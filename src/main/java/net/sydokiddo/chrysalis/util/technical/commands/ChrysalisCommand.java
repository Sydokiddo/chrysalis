package net.sydokiddo.chrysalis.util.technical.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.sydokiddo.chrysalis.Chrysalis;

public class ChrysalisCommand {

    /**
     * A command to configure various chrysalis properties.
     **/

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("chrysalis").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
            .then(Commands.literal("version").executes((commandContext) -> version(commandContext.getSource())))
            .then(Commands.literal("reload").executes((commandContext) -> reload(commandContext.getSource())))
        );
    }

    private static int version(CommandSourceStack commandSourceStack) {
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.chrysalis.version." + (Chrysalis.IS_DEBUG ? "debug" : "not_debug"), Chrysalis.MOD_VERSION), true);
        return 0;
    }

    private static int reload(CommandSourceStack commandSourceStack) {
        Chrysalis.registryAccess = commandSourceStack.getServer().registryAccess();
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.chrysalis.reload"), true);
        return 0;
    }
}