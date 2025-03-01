package net.sydokiddo.chrysalis.mixin.entities.misc;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameRules;
import net.sydokiddo.chrysalis.util.entities.EncounterMusicMob;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique private Player chrysalis$player = (Player) (Object) this;
    @Unique private final String chrysalis$encounteredMobUuidTag = "encountered_mob_uuid";

    /**
     * Adds new data to players.
     **/

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void chrysalis$definePlayerTags(SynchedEntityData.Builder builder, CallbackInfo info) {
        builder.define(ChrysalisRegistry.ENCOUNTERED_MOB_UUID, Optional.empty());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void chrysalis$addPlayerTags(CompoundTag compoundTag, CallbackInfo info) {
        Optional<UUID> uUID = EntityDataHelper.getEncounteredMobUUID(this.chrysalis$player);
        uUID.ifPresent(value -> compoundTag.putUUID(this.chrysalis$encounteredMobUuidTag, value));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void chrysalis$readPlayerTags(CompoundTag compoundTag, CallbackInfo info) {
        if (compoundTag.get(this.chrysalis$encounteredMobUuidTag) != null) EntityDataHelper.setEncounteredMobUUID(this.chrysalis$player, compoundTag.getUUID(this.chrysalis$encounteredMobUuidTag));
    }

    /**
     * Clears an encounter mob's music if the player goes outside their range.
     **/

    @Unique private boolean chrysalis$shouldClearMusic = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void chrysalis$checkNearbyEncounteredMobs(CallbackInfo info) {

        if (this.level().isClientSide()) return;
        Optional<UUID> encounteredMobUuid = EntityDataHelper.getEncounteredMobUUID(this.chrysalis$player);

        if (this.chrysalis$shouldClearMusic && this.chrysalis$player instanceof ServerPlayer serverPlayer) {
            EntityDataHelper.setEncounteredMobUUID(serverPlayer, null);
            EventHelper.clearMusicOnServer(serverPlayer, true);
            this.chrysalis$shouldClearMusic = false;
        }

        if (encounteredMobUuid.isPresent()) {

            List<? extends Mob> nearbyEncounteredMobs = this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(128.0D), entity -> {
                boolean defaultReturnValue = entity instanceof EncounterMusicMob encounterMusicMob && entity.isAlive() && entity.getUUID() == encounteredMobUuid.get() && entity.distanceTo(this.chrysalis$player) <= encounterMusicMob.chrysalis$getFinalEncounterMusicRange();
                if (entity.getType().is(ChrysalisTags.ALWAYS_PLAYS_ENCOUNTER_MUSIC)) return defaultReturnValue;
                else return defaultReturnValue && entity.getTarget() != null;
            });

            if (nearbyEncounteredMobs.isEmpty()) this.chrysalis$shouldClearMusic = true;
        }
    }

    /**
     * Allows for blocks in the allows_use_while_sneaking to be interacted with the maximum priority even while sneaking.
     **/

    @Unique
    private BlockHitResult chrysalis$getPlayerPOVHitResult() {

        Vec3 eyePosition = this.getEyePosition();

        float xCos = -Mth.cos(-this.getXRot() * 0.017453292F);
        float xSin = Mth.sin(-this.getXRot() * 0.017453292F);

        float yCos = Mth.cos(-this.getYRot() * 0.017453292F - 3.1415927F);
        float ySin = Mth.sin(-this.getYRot() * 0.017453292F - 3.1415927F);

        float sinTimesCos = ySin * xCos;
        float cosTimesCos = yCos * xCos;

        Vec3 vec3 = eyePosition.add((double) sinTimesCos * 5.0D, (double) xSin * 5.0D, (double) cosTimesCos * 5.0D);
        return this.level().clip(new ClipContext(eyePosition, vec3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));
    }

    @Inject(method = "isSecondaryUseActive", at = @At(value = "RETURN"), cancellable = true)
    private void chrysalis$allowBlockUseWhileSneaking(CallbackInfoReturnable<Boolean> cir) {
        BlockHitResult blockHitResult = this.chrysalis$getPlayerPOVHitResult();
        if (this.getMainHandItem().isEmpty() && this.level().getBlockState(blockHitResult.getBlockPos()).is(ChrysalisTags.ALLOWS_USE_WHILE_SNEAKING)) cir.setReturnValue(false);
    }

    /**
     * Plays a sound when the player drops an item.
     **/

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At(value = "TAIL"))
    private void chrysalis$playInventoryItemDroppingSound(ItemStack itemStack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if (!itemStack.isEmpty() && retainOwnership) EntityDataHelper.playItemDroppingSound(this.chrysalis$player);
    }

    /**
     * If the playerDeathItemDespawning game rule is set to false, items dropped from players on death will never despawn.
     **/

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"))
    private void chrysalis$setDeathItemsToNeverDespawn(Inventory inventory) {
        if (this.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(ChrysalisGameRules.RULE_PLAYER_DEATH_ITEM_DESPAWNING)) {
            inventory.dropAll();
        } else {
            inventory.compartments.forEach((getInventory) -> getInventory.forEach(itemStack -> {

                ItemEntity droppedItem = inventory.player.drop(itemStack, true, false);
                inventory.removeItem(itemStack);

                CompoundTag compoundTag = new CompoundTag();

                if (droppedItem != null) {
                    droppedItem.addAdditionalSaveData(compoundTag);
                    if (inventory.player.isDeadOrDying()) compoundTag.putInt("Age", -32768);
                    droppedItem.readAdditionalSaveData(compoundTag);
                }
            }));
        }
    }

    /**
     * Any living entity in the hidden_from_statistics_menu tag will not show up in the statistics menu if the player kills it.
     **/

    @Inject(method = "killedEntity", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$hideEntityKilledStat(ServerLevel serverLevel, LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity != null && livingEntity.getType().is(ChrysalisTags.HIDDEN_FROM_STATISTICS_MENU)) cir.setReturnValue(true);
    }

    @Mixin(ServerPlayer.class)
    public abstract static class ServerPlayerMixin extends Player {

        private ServerPlayerMixin(Level level, BlockPos blockPos, float rotation, GameProfile gameProfile) {
            super(level, blockPos, rotation, gameProfile);
        }

        /**
         * Redirects the hurtServer method to respect the new damage capacity attribute.
         **/

        @Redirect(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
        private boolean chrysalis$hurtPlayerWithDamageCap(Player player, ServerLevel serverLevel, DamageSource damageSource, float damageAmount) {
            return super.hurtServer(serverLevel, damageSource, EntityDataHelper.getDamageCap(this, damageSource, damageAmount));
        }

        /**
         * Any living entity in the hidden_from_statistics_menu tag will not show up in the statistics menu if the player is killed by it.
         **/

        @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/stats/Stat;)V", ordinal = 0))
        private void chrysalis$hideEntityKilledByStat(ServerPlayer serverPlayer, Stat<?> stat) {
            LivingEntity killCredit = this.getKillCredit();
            if (killCredit != null && !killCredit.getType().is(ChrysalisTags.HIDDEN_FROM_STATISTICS_MENU)) this.awardStat(Stats.ENTITY_KILLED_BY.get(killCredit.getType()));
        }
    }
}