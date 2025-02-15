package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class OxygenCommand {

    /**
     * A command to configure an entity's oxygen values.
     **/

    private static final String
        targetString = "target",
        oxygenAmountString = "oxygen_amount"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("oxygen").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).then(Commands.argument(targetString, EntityArgument.entity())

            .then(Commands.literal("add")
                .then(Commands.argument(oxygenAmountString, IntegerArgumentType.integer(1))
                .executes((commandContext) -> addOxygen(commandContext.getSource(), EntityArgument.getEntity(commandContext, targetString), IntegerArgumentType.getInteger(commandContext, oxygenAmountString))))
            )

            .then(Commands.literal("remove")
                .then(Commands.argument(oxygenAmountString, IntegerArgumentType.integer(1))
                .executes((commandContext) -> removeOxygen(commandContext.getSource(), EntityArgument.getEntity(commandContext, targetString), IntegerArgumentType.getInteger(commandContext, oxygenAmountString))))
            )

            .then(Commands.literal("set")
                .then(Commands.argument(oxygenAmountString, IntegerArgumentType.integer(0))
                .executes((commandContext) -> setOxygen(commandContext.getSource(), EntityArgument.getEntity(commandContext, targetString), IntegerArgumentType.getInteger(commandContext, oxygenAmountString))))
            )
        ));
    }

    private static final SimpleCommandExceptionType fail = new SimpleCommandExceptionType(Component.translatable("gui.chrysalis.commands.oxygen.fail"));

    private static int addOxygen(CommandSourceStack commandSourceStack, Entity entity, int oxygenToAdd) throws CommandSyntaxException {

        if (!(entity instanceof LivingEntity livingEntity)) throw fail.create();

        int setOxygenValue = livingEntity.getAirSupply() + oxygenToAdd;
        int oxygenMissing = livingEntity.getMaxAirSupply() - livingEntity.getAirSupply();
        int finalOxygenAmount;

        if (oxygenToAdd > oxygenMissing) {
            setOxygenValue = livingEntity.getMaxAirSupply();
            finalOxygenAmount = oxygenMissing;
        } else {
            finalOxygenAmount = oxygenToAdd;
        }

        livingEntity.setAirSupply(setOxygenValue);
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.oxygen.add", finalOxygenAmount, livingEntity.getDisplayName()), true);
        return 1;
    }

    private static int removeOxygen(CommandSourceStack commandSourceStack, Entity entity, int oxygenToRemove) throws CommandSyntaxException {

        if (!(entity instanceof LivingEntity livingEntity)) throw fail.create();

        int setOxygenValue = livingEntity.getAirSupply() - oxygenToRemove;
        int oxygenSupply = livingEntity.getAirSupply();
        int finalOxygenAmount;

        if (oxygenToRemove > oxygenSupply) {
            setOxygenValue = 0;
            finalOxygenAmount = Math.max(oxygenSupply, 0);
        } else {
            finalOxygenAmount = oxygenToRemove;
        }

        livingEntity.setAirSupply(setOxygenValue);
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.oxygen.remove", finalOxygenAmount, livingEntity.getDisplayName()), true);
        return 1;
    }

    private static int setOxygen(CommandSourceStack commandSourceStack, Entity entity, int oxygenToSet) throws CommandSyntaxException {

        if (!(entity instanceof LivingEntity livingEntity)) throw fail.create();

        int setOxygenValue = Math.min(oxygenToSet, livingEntity.getMaxAirSupply());
        livingEntity.setAirSupply(setOxygenValue);
        commandSourceStack.sendSuccess(() -> Component.translatable("gui.chrysalis.commands.oxygen.set", livingEntity.getDisplayName(), setOxygenValue), true);
        return 1;
    }
}