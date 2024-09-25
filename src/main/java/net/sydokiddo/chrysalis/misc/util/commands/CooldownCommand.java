package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class CooldownCommand {

    private static final String
        targetString = "target",
        itemString = "item",
        lengthString = "length"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {

        commandDispatcher.register(Commands.literal("cooldown").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).then(Commands.argument(targetString, EntityArgument.player())

            .then(Commands.literal("add")
                .then(Commands.argument(itemString, ItemArgument.item(commandBuildContext))
                .then(Commands.argument(lengthString, IntegerArgumentType.integer(1))
                .executes((commandContext) -> addCooldown(commandContext.getSource(), EntityArgument.getPlayer(commandContext, targetString), ItemArgument.getItem(commandContext, itemString), IntegerArgumentType.getInteger(commandContext, lengthString)))))
            )

            .then(Commands.literal("remove")
                .then(Commands.argument(itemString, ItemArgument.item(commandBuildContext))
                .executes((commandContext) -> removeCooldown(commandContext.getSource(), EntityArgument.getPlayer(commandContext, targetString), ItemArgument.getItem(commandContext, itemString))))
            )
        ));
    }

    private static int addCooldown(CommandSourceStack commandSourceStack, Player player, ItemInput itemInput, int cooldownLength) {
        player.getCooldowns().addCooldown(itemInput.getItem(), cooldownLength);
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.cooldown.add", cooldownLength, itemInput.getItem().getDefaultInstance().getDisplayName(), player.getDisplayName()), true);
        return 1;
    }

    private static int removeCooldown(CommandSourceStack commandSourceStack, Player player, ItemInput itemInput) {
        player.getCooldowns().removeCooldown(itemInput.getItem());
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.cooldown.remove", itemInput.getItem().getDefaultInstance().getDisplayName(), player.getDisplayName()), true);
        return 1;
    }
}