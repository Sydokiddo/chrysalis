package net.sydokiddo.chrysalis.util.entities;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.ChrysalisRegistry;
import net.sydokiddo.chrysalis.common.misc.ChrysalisAttributes;
import net.sydokiddo.chrysalis.common.misc.ChrysalisSoundEvents;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes.BuildPreventingEffect;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class EntityDataHelper {

    /**
     * Accesses various pieces of data from entities.
     **/

    // region General Entity Data

    public static boolean isPlayerStarving(ServerPlayer serverPlayer) {
        return serverPlayer.getFoodData().getFoodLevel() <= 6.0F && !serverPlayer.getAbilities().instabuild;
    }

    public static boolean isEntityMoving(LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) return player.getKnownMovement().horizontalDistanceSqr() > 0.0D;
        return livingEntity.getDeltaMovement().horizontalDistanceSqr() > 0.0D;
    }

    public static boolean isMobWithAIMoving(Mob mob) {
        return isEntityMoving(mob) && !mob.isNoAi() && !mob.isPassenger();
    }

    public static boolean isEntityInFluid(LivingEntity livingEntity) {
        return livingEntity.isInLiquid() || livingEntity.isInPowderSnow;
    }

    public static boolean targetIsImmunePlayer(Entity target, Entity player) {
        return target instanceof Player playerTarget && player instanceof Player playerOwner && !playerOwner.canHarmPlayer(playerTarget);
    }

    public static boolean targetIsAttachedToLead(Entity target, Player player) {
        return target instanceof Mob mob && mob.isLeashed() && mob.getLeashHolder() == player;
    }

    public static boolean targetIsLinkedAllay(Entity target, Player player) {
        if (!(target instanceof Allay allay)) return false;
        Optional<UUID> optional = allay.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
        return optional.isPresent() && player.getUUID().equals(optional.get());
    }

    public static boolean getCustomNameTagName(String name, Mob mob) {
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

        float damageCap = (float) livingEntity.getAttributeValue(ChrysalisAttributes.DAMAGE_CAPACITY);

        if (originalDamage > damageCap && originalDamage < Float.MAX_VALUE && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (Chrysalis.IS_DEBUG && !livingEntity.level().isClientSide()) Chrysalis.LOGGER.info("{} has taken damage higher than {}, setting damage amount to {}", livingEntity.getName().getString(), damageCap, damageCap);
            return damageCap;
        }

        return originalDamage;
    }

    public static void playItemDroppingSound(Player player) {
        if (CConfigOptions.ITEM_DROPPING_SOUND.get()) player.playNotifySound(ChrysalisSoundEvents.ITEM_DROP.get(), player.getSoundSource(), 0.2F, 0.5F + player.level().getRandom().nextFloat() * 0.5F);
    }

    public static Optional<UUID> getEncounteredMobUUID(Player player) {
        return player.getEntityData().get(ChrysalisRegistry.ENCOUNTERED_MOB_UUID);
    }

    public static void setEncounteredMobUUID(Player player, UUID uuid) {
        player.getEntityData().set(ChrysalisRegistry.ENCOUNTERED_MOB_UUID, Optional.ofNullable(uuid));
    }

    public static boolean hasBuildPreventingEffect(LivingEntity livingEntity) {
        Optional<MobEffectInstance> effectList = livingEntity.getActiveEffects().stream().findAny();
        return effectList.filter(mobEffectInstance -> mobEffectInstance.getEffect().value() instanceof BuildPreventingEffect).isPresent();
    }

    // endregion
}