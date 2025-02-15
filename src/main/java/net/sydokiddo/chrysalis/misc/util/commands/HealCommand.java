package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HealCommand {

    /**
     * A command to heal a selected entity.
     **/

    private static final String
        targetString = "target",
        amountString = "amount"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("heal").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
        .then(Commands.argument(targetString, EntityArgument.entity()).then(Commands.argument(amountString, FloatArgumentType.floatArg(0.1F))
        .executes((commandContext) -> heal(commandContext.getSource(), EntityArgument.getEntity(commandContext, targetString), FloatArgumentType.getFloat(commandContext, amountString))))));
    }

    private static int heal(CommandSourceStack commandSourceStack, Entity entity, float healAmount) throws CommandSyntaxException {

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.heal(healAmount);
            commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.heal.success", healAmount, entity.getDisplayName()), true);
            return 1;
        } else {
            throw new SimpleCommandExceptionType(Component.translatable("gui.chrysalis.commands.heal.fail")).create();
        }
    }
}