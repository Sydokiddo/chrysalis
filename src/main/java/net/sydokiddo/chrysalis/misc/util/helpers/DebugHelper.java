package net.sydokiddo.chrysalis.misc.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.Chrysalis;

@SuppressWarnings("unused")
public class DebugHelper {

    public static void sendEntityDespawnDebugMessage(LivingEntity livingEntity) {
        if (Chrysalis.IS_DEBUG && !livingEntity.level().isClientSide() && !livingEntity.isRemoved()) Chrysalis.LOGGER.info("{} has been removed from the world", livingEntity.getName().getString());
    }

    public static void sendEntityConversionDebugMessage(LivingEntity startingEntity, LivingEntity resultEntity) {
        if (Chrysalis.IS_DEBUG && !startingEntity.level().isClientSide()) Chrysalis.LOGGER.info("{} has been converted into {}", startingEntity.getName().getString(), resultEntity.getName().getString());
    }

    public static void sendAddAttributeDebugMessage(AttributeModifier attributeModifier, Mob mob) {
        if (Chrysalis.IS_DEBUG && !mob.level().isClientSide()) Chrysalis.LOGGER.info("Adding {} to {}", attributeModifier.toString(), mob.getName().getString());
    }

    public static void sendRemoveAttributeDebugMessage(AttributeModifier attributeModifier, Mob mob) {
        if (Chrysalis.IS_DEBUG && !mob.level().isClientSide()) Chrysalis.LOGGER.info("Removing {} from {}", attributeModifier.toString(), mob.getName().getString());
    }

    public static void sendWaxingDebugMessage(Level level, String blockString, Player player, BlockPos blockPos) {
        if (Chrysalis.IS_DEBUG && !level.isClientSide()) Chrysalis.LOGGER.info("{} has been waxed by {} at {}", blockString, player.getName().getString(), blockPos);
    }
}