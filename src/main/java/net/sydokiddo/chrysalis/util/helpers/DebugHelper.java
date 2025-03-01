package net.sydokiddo.chrysalis.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.ChrysalisMod;

@SuppressWarnings("unused")
public class DebugHelper {

    /**
     * Various template debug messages that can be sent in the logger.
     **/

    public static void sendEntityDespawnDebugMessage(LivingEntity livingEntity) {
        if (ChrysalisMod.IS_DEBUG && !livingEntity.level().isClientSide() && !livingEntity.isRemoved()) ChrysalisMod.LOGGER.info("{} has been removed from the world", livingEntity.getName().getString());
    }

    public static void sendEntityConversionDebugMessage(LivingEntity startingEntity, LivingEntity resultEntity) {
        if (ChrysalisMod.IS_DEBUG && !startingEntity.level().isClientSide()) ChrysalisMod.LOGGER.info("{} has been converted into {}", startingEntity.getName().getString(), resultEntity.getName().getString());
    }

    public static void sendAddAttributeDebugMessage(AttributeModifier attributeModifier, Mob mob) {
        if (ChrysalisMod.IS_DEBUG && !mob.level().isClientSide()) ChrysalisMod.LOGGER.info("Adding {} to {}", attributeModifier.toString(), mob.getName().getString());
    }

    public static void sendRemoveAttributeDebugMessage(AttributeModifier attributeModifier, Mob mob) {
        if (ChrysalisMod.IS_DEBUG && !mob.level().isClientSide()) ChrysalisMod.LOGGER.info("Removing {} from {}", attributeModifier.toString(), mob.getName().getString());
    }

    public static void sendWaxingDebugMessage(Level level, String blockString, Player player, BlockPos blockPos) {
        if (ChrysalisMod.IS_DEBUG && !level.isClientSide()) ChrysalisMod.LOGGER.info("{} has been waxed by {} at {}", blockString, player.getName().getString(), blockPos);
    }
}