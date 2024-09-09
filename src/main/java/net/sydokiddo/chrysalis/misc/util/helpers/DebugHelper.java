package net.sydokiddo.chrysalis.misc.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.Chrysalis;

@SuppressWarnings("unused")
public class DebugHelper {

    public static void sendEntityDespawnDebugMessage(LivingEntity livingEntity) {
        if (Chrysalis.IS_DEBUG && !livingEntity.level().isClientSide() && !livingEntity.isRemoved()) Chrysalis.LOGGER.info("Removing {} due to the config option being disabled", livingEntity.getName().getString());
    }

    public static void sendEntityConversionDebugMessage(LivingEntity startingEntity, LivingEntity resultEntity) {
        if (Chrysalis.IS_DEBUG && !startingEntity.level().isClientSide()) Chrysalis.LOGGER.info("{} has been converted into {}", startingEntity.getName().getString(), resultEntity.getName().getString());
    }

    public static void sendWaxedDebugMessage(Level level, String blockString, Player player, BlockPos blockPos) {
        if (Chrysalis.IS_DEBUG && !level.isClientSide()) Chrysalis.LOGGER.info("{} has been successfully waxed by {} at {}", blockString, player.getName().getString(), blockPos);
    }
}