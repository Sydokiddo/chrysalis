package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class GameEventCommand {

    private static final String
        causingEntityString = "causing_entity",
        gameEventString = "game_event",
        posString = "pos"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("gameevent").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
        .then(Commands.argument(causingEntityString, EntityArgument.entity())
        .then(Commands.argument(gameEventString, ResourceKeyArgument.key(Registries.GAME_EVENT))
        .then(Commands.argument(posString, Vec3Argument.vec3())
        .executes((commandContext) -> playGameEvent(commandContext.getSource(), EntityArgument.getEntity(commandContext, causingEntityString), getGameEvent(commandContext), Vec3Argument.getVec3(commandContext, posString)))))));
    }

    private static Holder.Reference<GameEvent> getGameEvent(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        return ResourceKeyArgument.resolveKey(commandContext, gameEventString, Registries.GAME_EVENT, new DynamicCommandExceptionType(object -> Component.translatableEscape("gui.chrysalis.commands.game_event.fail.invalid", object)));
    }

    private static int playGameEvent(CommandSourceStack commandSourceStack, Entity causingEntity, Holder<GameEvent> gameEvent, Vec3 position) {
        commandSourceStack.getLevel().gameEvent(causingEntity, gameEvent, position);
        if (commandSourceStack.getPlayer() != null) commandSourceStack.getPlayer().sendSystemMessage(Component.translatable("gui.chrysalis.commands.game_event.success", gameEvent.getRegisteredName(), causingEntity.getDisplayName()));
        return 1;
    }
}