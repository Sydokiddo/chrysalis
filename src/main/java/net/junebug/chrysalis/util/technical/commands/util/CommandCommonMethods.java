package net.junebug.chrysalis.util.technical.commands.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;

public interface CommandCommonMethods {

    static void sendFeedbackMessage(boolean requiresGameRule, ServerPlayer serverPlayer, Component message) {
        if (requiresGameRule && !serverPlayer.serverLevel().getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) return;
        serverPlayer.sendSystemMessage(message);
    }
}