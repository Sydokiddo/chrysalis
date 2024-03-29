package net.sydokiddo.chrysalis.misc.util.entities;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SuppressWarnings("unused")
public class EntityDataHelper {

    /**
     * Accesses various data from entities.
     **/

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

    public static boolean targetIsImmunePlayer(Entity self, Entity target) {
        return target instanceof Player playerTarget && self instanceof Player playerOwner && !playerOwner.canHarmPlayer(playerTarget);
    }

    public static boolean getCustomNameTagName(String name, Mob mob) {
        return (mob.hasCustomName() && name.equals(ChatFormatting.stripFormatting(mob.getName().getString())));
    }

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

    public static boolean isLookingUp(Entity entity) {
        return entity.getXRot() < -45;
    }

    public static boolean isLookingForward(Entity entity) {
        return entity.getXRot() > -45 && entity.getXRot() < 45;
    }

    public static boolean isLookingDown(Entity entity) {
        return entity.getXRot() > 45;
    }
}