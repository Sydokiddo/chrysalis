package net.sydokiddo.chrysalis.mixin.entities.misc;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.CRegistry;
import net.sydokiddo.chrysalis.common.items.CDataComponents;
import net.sydokiddo.chrysalis.common.misc.CAttributes;
import net.sydokiddo.chrysalis.common.misc.CGameRules;
import net.sydokiddo.chrysalis.util.entities.codecs.PlayerLootTableData;
import net.sydokiddo.chrysalis.util.entities.interfaces.EncounterMusicMob;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.common.misc.CTags;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique private Player chrysalis$player = (Player) (Object) this;
    @Unique private final String chrysalis$encounteredMobUuidTag = "encountered_mob_uuid";
    @Shadow protected abstract void touch(Entity entity);

    /**
     * Adds new data to players.
     **/

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void chrysalis$definePlayerTags(SynchedEntityData.Builder builder, CallbackInfo info) {
        builder.define(CRegistry.ENCOUNTERED_MOB_UUID, Optional.empty());
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

    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void chrysalis$addPlayerAttributes(final CallbackInfoReturnable<AttributeSupplier.Builder> info) {
        if (info.getReturnValue() != null) {
            info.getReturnValue().add(CAttributes.ITEM_PICK_UP_RANGE);
            info.getReturnValue().add(CAttributes.EXPERIENCE_PICK_UP_RANGE);
        }
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
                if (entity.getType().is(CTags.ALWAYS_PLAYS_ENCOUNTER_MUSIC)) return defaultReturnValue;
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
        if (this.getMainHandItem().isEmpty() && this.level().getBlockState(blockHitResult.getBlockPos()).is(CTags.ALLOWS_USE_WHILE_SNEAKING)) cir.setReturnValue(false);
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
        if (this.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(CGameRules.RULE_PLAYER_DEATH_ITEM_DESPAWNING)) {
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
     * Changes how players interact with the hitboxes of other entities, now respecting the item_pick_up_range and experience_pick_up_range attributes.
     **/

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"))
    private boolean chrysalis$cancelVanillaPlayerTouch(Player player) {
        return true;
    }

    @Inject(method = "aiStep", at = @At(value = "RETURN"))
    private void chrysalis$changePlayerTouch(CallbackInfo info) {

        AABB normalHitboxRange;
        AABB itemHitboxRange;
        AABB experienceHitboxRange;

        double itemPickUpRange = this.getAttributeValue(CAttributes.ITEM_PICK_UP_RANGE);
        double experiencePickUpRange = this.getAttributeValue(CAttributes.EXPERIENCE_PICK_UP_RANGE);

        if (this.getVehicle() != null && !this.getVehicle().isRemoved()) {
            normalHitboxRange = this.getBoundingBox().minmax(this.getVehicle().getBoundingBox()).inflate(1.0D, 0.0D, 1.0D);
            itemHitboxRange = this.getBoundingBox().minmax(this.getVehicle().getBoundingBox()).inflate(itemPickUpRange, 0.0D, itemPickUpRange);
            experienceHitboxRange = this.getBoundingBox().minmax(this.getVehicle().getBoundingBox()).inflate(experiencePickUpRange, 0.0D, experiencePickUpRange);
        } else {
            normalHitboxRange = this.getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
            itemHitboxRange = this.getBoundingBox().inflate(itemPickUpRange, itemPickUpRange / 2.0D, itemPickUpRange);
            experienceHitboxRange = this.getBoundingBox().inflate(experiencePickUpRange, experiencePickUpRange / 2.0D, experiencePickUpRange);
        }

        List<Entity> touchedEntities = this.level().getEntities(this, normalHitboxRange, entity -> !(entity instanceof ItemEntity itemEntity && itemEntity.getOwner() != this) && !(entity instanceof ExperienceOrb));
        List<ItemEntity> touchedItemEntities = this.level().getEntitiesOfClass(ItemEntity.class, itemHitboxRange, itemEntity -> itemEntity.getOwner() != this);
        List<ExperienceOrb> touchedExperienceOrbs = this.level().getEntitiesOfClass(ExperienceOrb.class, experienceHitboxRange);
        List<Entity> experienceList = Lists.newArrayList();

        for (Entity entity : touchedEntities) {
            if (!entity.isRemoved()) this.touch(entity);
        }

        for (ItemEntity itemEntity : touchedItemEntities) {
            if (!itemEntity.isRemoved()) this.touch(itemEntity);
        }

        experienceList.addAll(touchedExperienceOrbs);
        if (!experienceList.isEmpty()) this.touch(Util.getRandom(experienceList, this.getRandom()));
    }

    /**
     * Any living entity in the hidden_from_statistics_menu tag will not show up in the statistics menu if the player kills it.
     **/

    @Inject(method = "killedEntity", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$hideEntityKilledStat(ServerLevel serverLevel, LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity != null && livingEntity.getType().is(CTags.HIDDEN_FROM_STATISTICS_MENU)) cir.setReturnValue(true);
    }

    /**
     * Allows for custom loot tables to be dropped by specific players.
     **/

    @Override
    protected void dropCustomDeathLoot(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, boolean recentlyHit) {

        super.dropCustomDeathLoot(serverLevel, damageSource, recentlyHit);

        if (Chrysalis.registryAccess == null) return;
        List<PlayerLootTableData> list = Chrysalis.registryAccess.lookupOrThrow(CRegistry.PLAYER_LOOT_TABLE_DATA).stream().filter(codec -> Objects.equals(codec.uuid(), this.getStringUUID())).toList();

        for (PlayerLootTableData playerLootTableData : list) {

            if (list.size() > 1) playerLootTableData = list.getFirst();
            if (playerLootTableData.forTesting() && !Chrysalis.IS_DEBUG) return;
            Optional<ResourceKey<LootTable>> optional = Optional.ofNullable(playerLootTableData.lootTable());

            if (optional.isPresent()) {

                LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(optional.get());

                LootParams.Builder builder = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, this)
                .withParameter(LootContextParams.ORIGIN, this.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());

                if (damageSource.getEntity() instanceof Player && this.lastHurtByPlayer != null) builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());

                LootParams lootparams = builder.create(LootContextParamSets.ENTITY);
                lootTable.getRandomItems(lootparams, this.getLootTableSeed(), itemStack -> this.spawnAtLocation(serverLevel, itemStack));
                this.dropExperience(serverLevel, this);
            }
        }
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
            if (killCredit != null && !killCredit.getType().is(CTags.HIDDEN_FROM_STATISTICS_MENU)) this.awardStat(Stats.ENTITY_KILLED_BY.get(killCredit.getType()));
        }
    }

    @Mixin(Inventory.class)
    public static class InventoryMixin {

        @WrapOperation(method = "dropAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
        private boolean chrysalis$keepItemOnDeath(ItemStack itemStack, Operation<Boolean> original) {
            if (itemStack.has(CDataComponents.REMAINS_ON_DEATH) && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) return true;
            return itemStack.isEmpty();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(AbstractClientPlayer.class)
    public static abstract class AbstractClientPlayerMixin extends Player {

        @Shadow @Nullable protected abstract PlayerInfo getPlayerInfo();

        private AbstractClientPlayerMixin(Level level, BlockPos blockPos, float yRot, GameProfile gameProfile) {
            super(level, blockPos, yRot, gameProfile);
        }

        @Inject(at = @At("RETURN"), method = "getSkin", cancellable = true)
        private void chrysalis$customCapes(CallbackInfoReturnable<PlayerSkin> cir) {
            if (this.getPlayerInfo() == null) return;
            if (Chrysalis.IS_DEBUG) cir.setReturnValue(this.chrysalis$setCustomCape(Chrysalis.resourceLocationId("textures/entity/cape/chrysalis.png")));
            else if (Objects.equals(this.getPlayerInfo().getProfile().getId().toString(), "d92469c6-e198-4db5-99e3-759e84036aea")) cir.setReturnValue(this.chrysalis$setCustomCape(Chrysalis.resourceLocationId("textures/entity/cape/sydokiddo.png")));
        }

        @Unique
        private PlayerSkin chrysalis$setCustomCape(ResourceLocation capeLocation) {
            assert this.getPlayerInfo() != null;
            return new PlayerSkin(this.getPlayerInfo().getSkin().texture(), this.getPlayerInfo().getSkin().textureUrl(), capeLocation, this.getPlayerInfo().getSkin().elytraTexture(), this.getPlayerInfo().getSkin().model(), true);
        }
    }
}