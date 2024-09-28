package net.sydokiddo.chrysalis.misc.util.entities;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class EntityDataHelper {

    /**
     * Accesses various data from entities.
     **/

    // region General Entity Data

    public static boolean isPlayerStarving(ServerPlayer serverPlayer) {
        return serverPlayer.getFoodData().getFoodLevel() <= 6.0F && !serverPlayer.getAbilities().instabuild;
    }

    public static boolean isEntityMoving(LivingEntity livingEntity) {
        return livingEntity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
    }

    public static boolean isMobWithAIMoving(Mob mob) {
        return isEntityMoving(mob) && !mob.isNoAi() && !mob.isPassenger();
    }

    public static boolean isEntityInFluid(LivingEntity livingEntity) {
        return (livingEntity.isUnderWater() || livingEntity.isInPowderSnow || livingEntity.isInLava());
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
        return (mob.hasCustomName() && name.equals(ChatFormatting.stripFormatting(mob.getName().getString())));
    }

    // endregion

    // region Dimension Checks

    public static boolean isMobInOverworld(Entity entity) {
        return entity.level().dimension() == Level.OVERWORLD;
    }

    public static boolean isMobInNether(Entity entity) {
        return entity.level().dimension() == Level.NETHER;
    }

    public static boolean isMobInEnd(Entity entity) {
        return entity.level().dimension() == Level.END;
    }

    public static boolean isMobInDimension(Entity entity, ResourceKey<Level> dimension) {
        return entity.level().dimension() == dimension;
    }

    // endregion

    // region Look Direction Checks

    public static boolean isLookingUp(Entity entity) {
        return entity.getXRot() < -45;
    }

    public static boolean isLookingForward(Entity entity) {
        return entity.getXRot() > -45 && entity.getXRot() < 45;
    }

    public static boolean isLookingDown(Entity entity) {
        return entity.getXRot() > 45;
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

    // endregion
}