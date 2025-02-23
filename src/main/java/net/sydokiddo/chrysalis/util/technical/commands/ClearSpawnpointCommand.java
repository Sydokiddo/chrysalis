package net.sydokiddo.chrysalis.util.technical.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.util.technical.commands.util.CommandCommonMethods;
import java.util.Objects;

public class ClearSpawnpointCommand {

    /**
     * A command to clear a player's spawn point.
     **/

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("clearspawnpoint").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).executes(ClearSpawnpointCommand::clearSpawnpoint));
    }

    private static int clearSpawnpoint(CommandContext<CommandSourceStack> context) {

        Player player = Objects.requireNonNull(context.getSource().getPlayer());

        if (player instanceof ServerPlayer serverPlayer) {

            Component successText = Component.translatable("gui.chrysalis.commands.clear_spawnpoint.success", player.getDisplayName());
            Component failText = Component.translatable("gui.chrysalis.commands.clear_spawnpoint.fail", player.getDisplayName()).withStyle(ChatFormatting.RED);

            if (serverPlayer.getRespawnPosition() != null) {
                serverPlayer.setRespawnPosition(Level.OVERWORLD, null, 0.0F, false, false);
                CommandCommonMethods.sendFeedbackMessage(true, serverPlayer, successText);
            } else {
                CommandCommonMethods.sendFeedbackMessage(false, serverPlayer, failText);
            }
        }

        return 1;
    }
}