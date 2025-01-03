package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.sydokiddo.chrysalis.misc.util.helpers.EventHelper;
import java.util.Collection;

public class MusicCommand {

    private static final String
        targetsString = "targets",
        soundEventString = "sound_event",
        minDelayString = "min_delay",
        maxDelayString = "max_delay",
        replaceCurrentMusicString = "replace_current_music",
        fadeOutString = "fade_out"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("music").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).then(Commands.argument(targetsString, EntityArgument.players())

            .then(Commands.literal("queue")
                .then(Commands.argument(soundEventString, ResourceKeyArgument.key(Registries.SOUND_EVENT))
                .then(Commands.argument(minDelayString, IntegerArgumentType.integer(0))
                .then(Commands.argument(maxDelayString, IntegerArgumentType.integer(0))
                .then(Commands.argument(replaceCurrentMusicString, BoolArgumentType.bool())
                .executes((commandContext) -> queueMusic(commandContext.getSource(), EntityArgument.getPlayers(commandContext, targetsString),
                getSoundEvent(commandContext), IntegerArgumentType.getInteger(commandContext, minDelayString), IntegerArgumentType.getInteger(commandContext, maxDelayString),
                BoolArgumentType.getBool(commandContext, replaceCurrentMusicString)))))))
            )

            .then(Commands.literal("stop")
                .then(Commands.argument(fadeOutString, BoolArgumentType.bool())
                .executes((commandContext) -> stopMusic(commandContext.getSource(), EntityArgument.getPlayers(commandContext, targetsString), BoolArgumentType.getBool(commandContext, fadeOutString))))
            )
        ));
    }

    private static Holder.Reference<SoundEvent> getSoundEvent(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        return ResourceKeyArgument.resolveKey(commandContext, soundEventString, Registries.SOUND_EVENT, new DynamicCommandExceptionType(object -> Component.translatableEscape("gui.chrysalis.commands.music.queue.invalid", object)));
    }

    private static int queueMusic(CommandSourceStack commandSourceStack, Collection<? extends Player> players, Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {

        int returnValue = 0;
        Component successSingleMessage = Component.translatable("gui.chrysalis.commands.music.queue.success.single", soundEvent.getRegisteredName(), players.iterator().next().getDisplayName());
        Component successMultipleMessage = Component.translatable("gui.chrysalis.commands.music.queue.success.multiple", soundEvent.getRegisteredName(), players.size());

        for (Player player : players) {
            if (player instanceof ServerPlayer serverPlayer)
                EventHelper.sendMusic(serverPlayer, soundEvent, minDelay, maxDelay, replaceCurrentMusic);
            if (players.size() > 1) returnValue = 1;
        }

        if (returnValue == 0) commandSourceStack.sendSuccess(() -> successSingleMessage, true);
        else commandSourceStack.sendSuccess(() -> successMultipleMessage, true);
        return returnValue;
    }

    private static int stopMusic(CommandSourceStack commandSourceStack, Collection<? extends Player> players, boolean shouldFade) {

        int returnValue = 0;
        Component successSingleMessage = Component.translatable("gui.chrysalis.commands.music." + (shouldFade ? "stop.fade_out" : "stop") + ".success.single", players.iterator().next().getDisplayName());
        Component successMultipleMessage = Component.translatable("gui.chrysalis.commands.music." + (shouldFade ? "stop.fade_out" : "stop") + ".success.multiple", players.size());

        for (Player player : players) {
            if (player instanceof ServerPlayer serverPlayer) EventHelper.clearMusicOnServer(serverPlayer, shouldFade);
            if (players.size() > 1) returnValue = 1;
        }

        if (returnValue == 0) commandSourceStack.sendSuccess(() -> successSingleMessage, true);
        else commandSourceStack.sendSuccess(() -> successMultipleMessage, true);
        return returnValue;
    }
}