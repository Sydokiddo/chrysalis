package net.junebug.chrysalis.util.technical.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.junebug.chrysalis.util.technical.commands.util.CommandCommonMethods;
import java.util.Objects;

public class ShowcaseCommand {

    /**
     * A command to showcase the player's held item in the chat.
     **/

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("showcase").executes(ShowcaseCommand::displayItemToChat));
    }

    private static int displayItemToChat(CommandContext<CommandSourceStack> context) {

        ServerPlayer serverPlayer = Objects.requireNonNull(context.getSource().getPlayer());
        ItemStack itemStack = serverPlayer.getMainHandItem();

        Component successText = Component.translatable("gui.chrysalis.commands.showcase.success", serverPlayer.getDisplayName(), itemStack.getDisplayName());
        Component failText = Component.translatable("gui.chrysalis.commands.showcase.fail").withStyle(ChatFormatting.RED);

        if (!itemStack.isEmpty()) context.getSource().getServer().getPlayerList().broadcastSystemMessage(successText, false);
        else CommandCommonMethods.sendFeedbackMessage(false, serverPlayer, failText);
        return 1;
    }
}