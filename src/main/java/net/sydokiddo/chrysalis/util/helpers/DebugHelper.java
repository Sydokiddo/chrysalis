package net.sydokiddo.chrysalis.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

@SuppressWarnings("unused")
public class DebugHelper {

    /**
     * Various template debug messages that can be sent in the logger.
     **/

    // region Technical

    public static void sendInitializedMessage(Logger logger, String modVersion, boolean clientSide) {
        String dist;
        if (clientSide) dist = "Client-Side";
        else dist = "Server-Side";
        logger.info("{} v{} has been initialized! ({})", logger.getName(), modVersion, dist);
    }

    public static void sendLoggedInMessage(Logger logger, ServerPlayer serverPlayer, String modName, String modVersion) {
        logger.info("{} has {} v{} installed", serverPlayer.getName().getString(), modName, modVersion);
    }

    // endregion

    // region Entities

    public static void sendEntityDespawnMessage(Logger logger, boolean shouldSend, LivingEntity livingEntity) {
        if (shouldSend && !livingEntity.level().isClientSide() && !livingEntity.isRemoved()) logger.info("{} has been removed from the world", livingEntity.getName().getString());
    }

    public static void sendEntityConversionMessage(Logger logger, boolean shouldSend, LivingEntity startingEntity, LivingEntity resultEntity) {
        if (shouldSend && !startingEntity.level().isClientSide()) logger.info("{} has been converted into {}", startingEntity.getName().getString(), resultEntity.getName().getString());
    }

    public static void sendAttributeAddedMessage(Logger logger, boolean shouldSend, AttributeModifier attributeModifier, Mob mob) {
        if (shouldSend && !mob.level().isClientSide()) logger.info("Adding {} to {}", attributeModifier.toString(), mob.getName().getString());
    }

    public static void sendAttributeRemovedMessage(Logger logger, boolean shouldSend, AttributeModifier attributeModifier, Mob mob) {
        if (shouldSend && !mob.level().isClientSide()) logger.info("Removing {} from {}", attributeModifier.toString(), mob.getName().getString());
    }

    // endregion

    // region Blocks

    public static void sendWaxedMessage(Logger logger, boolean shouldSend, String blockName, Player player, BlockPos blockPos) {
        if (shouldSend && !player.level().isClientSide()) logger.info("{} has been waxed by {} at {}", blockName, player.getName().getString(), blockPos);
    }

    public static void sendDispenserMessage(Logger logger, boolean shouldSend, String dispensedObject, BlockPos blockPos) {
        if (shouldSend) logger.info("{} has been successfully dispensed at {}", dispensedObject, blockPos);
    }

    // endregion
}