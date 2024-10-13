package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.Objects;

public class ShowcaseCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("showcase").executes(ShowcaseCommand::displayItemToChat));
    }

    private static int displayItemToChat(CommandContext<CommandSourceStack> context) {

        Player player = Objects.requireNonNull(context.getSource().getPlayer());
        ItemStack itemStack = player.getMainHandItem();

        Component successText = Component.translatable("gui.chrysalis.commands.showcase.success", player.getDisplayName(), itemStack.getDisplayName());
        Component failText = Component.translatable("gui.chrysalis.commands.showcase.fail").withStyle(ChatFormatting.RED);

        if (!itemStack.isEmpty()) context.getSource().getServer().getPlayerList().broadcastSystemMessage(successText, false);
        else player.sendSystemMessage(failText);

        return 1;
    }
}