package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import java.util.Objects;

public class ClearSpawnpointCommand {

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
                player.sendSystemMessage(successText);
            } else {
                player.sendSystemMessage(failText);
            }
        }

        return 1;
    }
}