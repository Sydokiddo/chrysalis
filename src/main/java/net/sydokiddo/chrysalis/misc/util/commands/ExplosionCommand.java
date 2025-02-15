package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExplosionCommand {

    /**
     * A command to trigger a custom explosion.
     **/

    private static final String
        causingEntityString = "causing_entity",
        noneString = "none",
        posString = "pos",
        powerString = "power",
        createsFireString = "creates_fire"
    ;

    private static final float
        minExplosionPower = 0.1F,
        maxExplosionPower = 127.0F
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("explosion").requires((commandSourceStack) -> commandSourceStack.hasPermission(2))

            .then(Commands.argument(causingEntityString, EntityArgument.entity())
                .then(Commands.argument(posString, Vec3Argument.vec3())
                .then(Commands.argument(powerString, FloatArgumentType.floatArg(minExplosionPower, maxExplosionPower))
                .then(Commands.argument(createsFireString, BoolArgumentType.bool())
                .executes((commandContext) -> createExplosion(commandContext.getSource(), EntityArgument.getEntity(commandContext, causingEntityString),
                Vec3Argument.getVec3(commandContext, posString), FloatArgumentType.getFloat(commandContext, powerString), BoolArgumentType.getBool(commandContext, createsFireString))))))
            )

            .then(Commands.literal(noneString)
                .then(Commands.argument(posString, Vec3Argument.vec3())
                .then(Commands.argument(powerString, FloatArgumentType.floatArg(minExplosionPower, maxExplosionPower))
                .then(Commands.argument(createsFireString, BoolArgumentType.bool())
                .executes((commandContext) -> createExplosion(commandContext.getSource(), null,
                Vec3Argument.getVec3(commandContext, posString), FloatArgumentType.getFloat(commandContext, powerString), BoolArgumentType.getBool(commandContext, createsFireString))))))
            )
        );
    }

    private static int createExplosion(CommandSourceStack commandSourceStack, Entity causingEntity, Vec3 position, float power, boolean createsFire) {

        Component successText = Component.translatable("gui.chrysalis.commands.explosion.success", power);
        Level.ExplosionInteraction explosionInteraction = Level.ExplosionInteraction.BLOCK;

        if (causingEntity != null) {
            successText = Component.translatable("gui.chrysalis.commands.explosion.success.entity", causingEntity.getDisplayName(), power);
            explosionInteraction = Level.ExplosionInteraction.MOB;
        }

        commandSourceStack.getLevel().explode(causingEntity, Explosion.getDefaultDamageSource(commandSourceStack.getLevel(), causingEntity), null, position.x(), position.y(), position.z(), power, createsFire, explosionInteraction);
        if (commandSourceStack.getPlayer() != null) commandSourceStack.getPlayer().sendSystemMessage(successText);
        return 1;
    }
}