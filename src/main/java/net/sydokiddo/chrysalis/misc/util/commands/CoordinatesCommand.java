package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.blaze3d.platform.ClipboardManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.misc.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import java.util.Objects;

public class CoordinatesCommand {

    /**
     * A command to either copy the player's coordinates or convert them between dimensions.
     **/

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("coordinates")
            .then(Commands.literal("copy").executes(CoordinatesCommand::copyCoordinates))
            .then(Commands.literal("convert").executes(CoordinatesCommand::convertCoordinates))
        );
    }

    private static int copyCoordinates(CommandContext<CommandSourceStack> context) {
        ServerPlayer serverPlayer = Objects.requireNonNull(context.getSource().getPlayer());
        new ClipboardManager().setClipboard(0, Component.translatable("gui.chrysalis.coordinates", serverPlayer.getBlockX(), serverPlayer.getBlockY(), serverPlayer.getBlockZ()).getString());
        serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.commands.coordinates.copy.success", serverPlayer.getDisplayName()));
        return 0;
    }

    private static int convertCoordinates(CommandContext<CommandSourceStack> context) {

        ServerPlayer serverPlayer = Objects.requireNonNull(context.getSource().getPlayer());

        String dimensionString;
        int x = serverPlayer.getBlockX();
        int y = serverPlayer.getBlockY();
        int z = serverPlayer.getBlockZ();

        if (EntityDataHelper.isInOverworld(serverPlayer)) {
            dimensionString = Level.NETHER.location().toString();
            x = x / 8;
            z = z / 8;
        } else if (EntityDataHelper.isInNether(serverPlayer)) {
            dimensionString = Level.OVERWORLD.location().toString();
            x = x * 8;
            z = z * 8;
        } else {
            serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.commands.coordinates.convert.fail", serverPlayer.getDisplayName()).withStyle(ChatFormatting.RED));
            return 0;
        }

        serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.commands.coordinates.convert.success", ItemHelper.getDimensionComponent(dimensionString).getString(), Component.translatable("gui.chrysalis.coordinates", x, y, z).getString()));
        return 1;
    }
}