package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.sydokiddo.chrysalis.misc.util.helpers.EventHelper;
import java.util.Collection;

public class CameraShakeCommand {

    private static final String
        targetsString = "targets",
        resetString = "reset",
        timeString = "time",
        strengthString = "strength",
        frequencyString = "frequency"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("camerashake").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).then(Commands.argument(targetsString, EntityArgument.players())

            .then(Commands.argument(timeString, IntegerArgumentType.integer(1))
                .then(Commands.argument(strengthString, IntegerArgumentType.integer(1))
                .then(Commands.argument(frequencyString, IntegerArgumentType.integer(1))
                .executes((commandContext) -> shakeCamera(commandContext.getSource(), EntityArgument.getPlayers(commandContext, targetsString),
                IntegerArgumentType.getInteger(commandContext, timeString), IntegerArgumentType.getInteger(commandContext, strengthString), IntegerArgumentType.getInteger(commandContext, frequencyString)))))
            )

            .then(Commands.literal(resetString)
                .executes((commandContext) -> resetCameraShake(commandContext.getSource(), EntityArgument.getPlayers(commandContext, targetsString)))
            )
        ));
    }

    private static int shakeCamera(CommandSourceStack commandSourceStack, Collection<? extends Player> players, int time, int strength, int frequency) {

        int returnValue = 0;
        Component successSingleMessage = Component.translatable("gui.chrysalis.commands.camera_shake.success.single", players.iterator().next().getDisplayName());
        Component successMultipleMessage = Component.translatable("gui.chrysalis.commands.camera_shake.success.multiple", players.size());

        for (Player player : players) {
            if (player instanceof ServerPlayer serverPlayer) EventHelper.sendCameraShake(serverPlayer, time, strength, frequency);
            if (players.size() > 1) returnValue = 1;
        }

        if (returnValue == 0) commandSourceStack.sendSuccess(() -> successSingleMessage, true);
        else commandSourceStack.sendSuccess(() -> successMultipleMessage, true);
        return returnValue;
    }

    private static int resetCameraShake(CommandSourceStack commandSourceStack, Collection<? extends Player> players) {

        int returnValue = 0;
        Component successSingleMessage = Component.translatable("gui.chrysalis.commands.camera_shake.reset.success.single", players.iterator().next().getDisplayName());
        Component successMultipleMessage = Component.translatable("gui.chrysalis.commands.camera_shake.reset.success.multiple", players.size());

        for (Player player : players) {
            if (player instanceof ServerPlayer serverPlayer) EventHelper.resetCameraShake(serverPlayer);
            if (players.size() > 1) returnValue = 1;
        }

        if (returnValue == 0) commandSourceStack.sendSuccess(() -> successSingleMessage, true);
        else commandSourceStack.sendSuccess(() -> successMultipleMessage, true);
        return returnValue;
    }
}