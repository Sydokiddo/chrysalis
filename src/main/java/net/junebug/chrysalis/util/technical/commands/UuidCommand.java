package net.junebug.chrysalis.util.technical.commands;

import com.mojang.blaze3d.platform.ClipboardManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class UuidCommand {

    /**
     * A command to either print an entity's UUID or copy it to the clipboard.
     **/

    private static final String targetString = "target";

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("uuid").then(Commands.argument(targetString, EntityArgument.entity())
            .then(Commands.literal("print").executes((commandContext) -> printUUID(commandContext.getSource(), EntityArgument.getEntity(commandContext, targetString))))
            .then(Commands.literal("copy").executes((commandContext) -> copyUUID(commandContext.getSource(), EntityArgument.getEntity(commandContext, targetString)))
        )));
    }

    private static int printUUID(CommandSourceStack commandSourceStack, Entity entity) {
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.uuid.print.success", entity.getDisplayName(), entity.getStringUUID()), true);
        return 0;
    }

    private static int copyUUID(CommandSourceStack commandSourceStack, Entity entity) {
        new ClipboardManager().setClipboard(0, entity.getStringUUID());
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.uuid.copy.success", entity.getDisplayName()), true);
        return 0;
    }
}