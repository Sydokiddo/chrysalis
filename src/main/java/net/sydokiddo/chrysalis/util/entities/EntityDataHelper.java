package net.sydokiddo.chrysalis.util.entities;

import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.CRegistry;
import net.sydokiddo.chrysalis.common.misc.CAttributes;
import net.sydokiddo.chrysalis.common.misc.CSoundEvents;
import net.sydokiddo.chrysalis.common.misc.CTags;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.BuildPreventingEffect;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class EntityDataHelper {

    /**
     * Accesses various pieces of data from entities.
     **/

    // region General Entity Data

    public static boolean isPlayerStarving(ServerPlayer serverPlayer) {
        return !serverPlayer.getAbilities().instabuild && serverPlayer.getFoodData().getFoodLevel() <= 6.0F;
    }

    public static boolean isMoving(Entity entity) {
        return entity.getDeltaMovement().horizontalDistanceSqr() > 0.0D;
    }

    public static boolean isLivingEntityMoving(LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) return player.getKnownMovement().horizontalDistanceSqr() > 0.0D;
        else return isMoving(livingEntity);
    }

    public static boolean isMobMoving(Mob mob) {
        return isLivingEntityMoving(mob) && !mob.isNoAi() && !mob.isPassenger();
    }

    public static boolean isInFluid(LivingEntity livingEntity) {
        return livingEntity.isInLiquid() || livingEntity.isInPowderSnow;
    }

    public static boolean isTargetImmunePlayer(Entity target, Entity owner) {
        return target instanceof Player targetedPlayer && owner instanceof Player player && !player.canHarmPlayer(targetedPlayer);
    }

    public static boolean isTargetAttachedToLead(Entity target, Player leadHolder) {
        return target instanceof Mob mob && mob.isLeashed() && mob.getLeashHolder() == leadHolder;
    }

    public static boolean isTargetLinkedAllay(Entity target, Player player) {
        if (!(target instanceof Allay allay)) return false;
        Optional<UUID> optional = allay.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
        return optional.isPresent() && player.getUUID().equals(optional.get());
    }

    public static boolean getCustomName(Mob mob, String name) {
        return mob.hasCustomName() && name.equals(ChatFormatting.stripFormatting(mob.getName().getString()));
    }

    public static boolean isPlayerWithUUID(LivingEntity livingEntity, String uuid) {
        return livingEntity instanceof Player player && player.getStringUUID().equals(uuid);
    }

    // endregion

    // region Dimension Checks

    public static boolean isInOverworld(Entity entity) {
        return isInDimension(entity, Level.OVERWORLD);
    }

    public static boolean isInNether(Entity entity) {
        return isInDimension(entity, Level.NETHER);
    }

    public static boolean isInEnd(Entity entity) {
        return isInDimension(entity, Level.END);
    }

    public static boolean isInDimension(Entity entity, ResourceKey<Level> dimension) {
        return entity.level().dimension() == dimension;
    }

    // endregion

    // region Look Direction Checks

    public static boolean isLookingUp(Entity entity) {
        return entity.getXRot() < -45.0F;
    }

    public static boolean isLookingForward(Entity entity) {
        return entity.getXRot() > -45.0F && entity.getXRot() < 45.0F;
    }

    public static boolean isLookingDown(Entity entity) {
        return entity.getXRot() > 45.0F;
    }

    // endregion

    // region Miscellaneous

    public static AABB setHitboxSize(Entity entity, float width, float height) {
        double x = entity.position().x();
        double y = entity.position().y();
        double z = entity.position().z();
        float dividedWidth = width / 2.0F;
        return new AABB(x - (double) dividedWidth, y, z - (double) dividedWidth, x + (double) dividedWidth, y + (double) height, z + (double) dividedWidth);
    }

    public static float getDamageCap(LivingEntity livingEntity, DamageSource damageSource, float originalDamage) {

        float damageCap = (float) livingEntity.getAttributeValue(CAttributes.DAMAGE_CAPACITY);

        if (originalDamage > damageCap && originalDamage < Float.MAX_VALUE && !damageSource.is(CTags.BYPASSES_DAMAGE_CAPACITY)) {
            if (Chrysalis.IS_DEBUG && !livingEntity.level().isClientSide()) Chrysalis.LOGGER.info("{} has taken damage higher than {}, setting damage amount to {}", livingEntity.getName().getString(), damageCap, damageCap);
            return damageCap;
        }

        return originalDamage;
    }

    public static void playItemDroppingSound(Player player) {
        if (CConfigOptions.ITEM_DROPPING_SOUND.get()) player.playNotifySound(CSoundEvents.ITEM_DROP.get(), player.getSoundSource(), 0.2F, 0.5F + player.level().getRandom().nextFloat() * 0.5F);
    }

    public static Optional<UUID> getEncounteredMobUUID(Player player) {
        return player.getEntityData().get(CRegistry.ENCOUNTERED_MOB_UUID);
    }

    public static void setEncounteredMobUUID(Player player, UUID uuid) {
        player.getEntityData().set(CRegistry.ENCOUNTERED_MOB_UUID, Optional.ofNullable(uuid));
    }

    public static boolean hasBuildPreventingEffect(LivingEntity livingEntity) {
        Optional<MobEffectInstance> effectList = livingEntity.getActiveEffects().stream().findAny();
        return effectList.filter(mobEffectInstance -> mobEffectInstance.getEffect().value() instanceof BuildPreventingEffect).isPresent();
    }

    public static void updateCurrentShader(ServerPlayer serverPlayer) {
        serverPlayer.connection.send(new ClientboundSetCameraPacket(serverPlayer));
    }

    public static List<ItemStack> getNonLivingEntityLootTable(Entity entity, ResourceKey<LootTable> lootTableKey) {
        LootTable lootTable = Objects.requireNonNull(entity.level().getServer()).reloadableRegistries().getLootTable(lootTableKey);
        if (!(entity.level() instanceof ServerLevel serverLevel)) return List.of();
        LootParams lootParams = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.THIS_ENTITY, entity).create(LootContextParamSets.GIFT);
        return lootTable.getRandomItems(lootParams);
    }

    public static void dropFromLootTable(Entity entity, ResourceKey<LootTable> lootTableKey) {
        List<ItemStack> lootTable = EntityDataHelper.getNonLivingEntityLootTable(entity, lootTableKey);
        if (entity.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) for (ItemStack itemStack : lootTable) entity.spawnAtLocation(serverLevel, itemStack);
    }

    // endregion
}