package net.junebug.chrysalis.util.technical.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import java.util.Collection;

public class VelocityCommand {

    /**
     * A command to configure an entity's velocity.
     **/

    private static final String
        targetsString = "targets",
        xString = "x",
        yString = "y",
        zString = "z"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("velocity").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).then(Commands.argument(targetsString, EntityArgument.entities())

            .then(Commands.literal("add")
                .then(Commands.argument(xString, DoubleArgumentType.doubleArg(0))
                .then(Commands.argument(yString, DoubleArgumentType.doubleArg(0))
                .then(Commands.argument(zString, DoubleArgumentType.doubleArg(0))
                .executes((commandContext) -> changeVelocity(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString), 1,
                DoubleArgumentType.getDouble(commandContext, xString), DoubleArgumentType.getDouble(commandContext, yString), DoubleArgumentType.getDouble(commandContext, zString))))))
            )

            .then(Commands.literal("subtract")
                .then(Commands.argument(xString, DoubleArgumentType.doubleArg(0))
                .then(Commands.argument(yString, DoubleArgumentType.doubleArg(0))
                .then(Commands.argument(zString, DoubleArgumentType.doubleArg(0))
                .executes((commandContext) -> changeVelocity(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString), 2,
                DoubleArgumentType.getDouble(commandContext, xString), DoubleArgumentType.getDouble(commandContext, yString), DoubleArgumentType.getDouble(commandContext, zString))))))
            )

            .then(Commands.literal("multiply")
                .then(Commands.argument(xString, DoubleArgumentType.doubleArg(0))
                .then(Commands.argument(yString, DoubleArgumentType.doubleArg(0))
                .then(Commands.argument(zString, DoubleArgumentType.doubleArg(0))
                .executes((commandContext) -> changeVelocity(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString), 3,
                DoubleArgumentType.getDouble(commandContext, xString), DoubleArgumentType.getDouble(commandContext, yString), DoubleArgumentType.getDouble(commandContext, zString))))))
            )

            .then(Commands.literal("set")
                .then(Commands.argument(xString, DoubleArgumentType.doubleArg(0))
                .then(Commands.argument(yString, DoubleArgumentType.doubleArg(0))
                .then(Commands.argument(zString, DoubleArgumentType.doubleArg(0))
                .executes((commandContext) -> changeVelocity(commandContext.getSource(), EntityArgument.getEntities(commandContext, targetsString), 0,
                DoubleArgumentType.getDouble(commandContext, xString), DoubleArgumentType.getDouble(commandContext, yString), DoubleArgumentType.getDouble(commandContext, zString))))))
            )
        ));
    }

    private static int changeVelocity(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, int type, double x, double y, double z) throws CommandSyntaxException {

        int returnValue = 0;
        Component successSingleMessage = Component.empty();
        Component successMultipleMessage = Component.empty();
        SimpleCommandExceptionType failGeneric = new SimpleCommandExceptionType(Component.translatable("gui.chrysalis.commands.velocity.fail"));

        for (Entity entity : entities) {

            switch (type) {
                case 1 -> {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(x, y, z));
                    successSingleMessage = Component.translatable("gui.chrysalis.commands.velocity.add.success.single", x, y, z, entities.iterator().next().getDisplayName());
                    successMultipleMessage = Component.translatable("gui.chrysalis.commands.velocity.add.success.multiple", x, y, z, entities.size());
                }
                case 2 -> {
                    entity.setDeltaMovement(entity.getDeltaMovement().subtract(x, y, z));
                    successSingleMessage = Component.translatable("gui.chrysalis.commands.velocity.subtract.success.single", x, y, z, entities.iterator().next().getDisplayName());
                    successMultipleMessage = Component.translatable("gui.chrysalis.commands.velocity.subtract.success.multiple", x, y, z, entities.size());
                }
                case 3 -> {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(x, y, z));
                    successSingleMessage = Component.translatable("gui.chrysalis.commands.velocity.multiply.success.single", x, y, z, entities.iterator().next().getDisplayName());
                    successMultipleMessage = Component.translatable("gui.chrysalis.commands.velocity.multiply.success.multiple", x, y, z, entities.size());
                }
                default -> {
                    entity.setDeltaMovement(x, y, z);
                    successSingleMessage = Component.translatable("gui.chrysalis.commands.velocity.set.success.single", x, y, z, entities.iterator().next().getDisplayName());
                    successMultipleMessage = Component.translatable("gui.chrysalis.commands.velocity.set.success.multiple", x, y, z, entities.size());
                }
            }

            ++returnValue;
        }

        Component finalSuccessSingleMessage = successSingleMessage;
        Component finalSuccessMultipleMessage = successMultipleMessage;

        if (returnValue == 0) throw failGeneric.create();
        if (entities.size() == 1) commandSourceStack.sendSuccess(() -> finalSuccessSingleMessage, true);
        else commandSourceStack.sendSuccess(() -> finalSuccessMultipleMessage, true);
        return returnValue;
    }
}