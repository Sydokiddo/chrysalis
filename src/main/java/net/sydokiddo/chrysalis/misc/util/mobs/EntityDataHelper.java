package net.sydokiddo.chrysalis.misc.util.mobs;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

@SuppressWarnings("unused")
public class EntityDataHelper {

    /**
     * Accesses various data from entities.
     **/

    public static boolean isPlayerStarving(ServerPlayer serverPlayer) {
        return serverPlayer.getFoodData().getFoodLevel() <= 6.0F;
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

    public static boolean getCustomNameTagName(String name, Mob mob) {
        return (mob.hasCustomName() && name.equals(ChatFormatting.stripFormatting(mob.getName().getString())));
    }

    public static boolean isMobInOverworld(Mob mob) {
        return mob.level() != null && mob.level().dimension() == Level.OVERWORLD;
    }

    public static boolean isMobInNether(Mob mob) {
        return mob.level() != null && mob.level().dimension() == Level.NETHER;
    }

    public static boolean isMobInEnd(Mob mob) {
        return mob.level() != null && mob.level().dimension() == Level.END;
    }
}