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

    private static final String
        causingEntityString = "causing_entity",
        posString = "pos",
        powerString = "power",
        createsFireString = "creates_fire"
    ;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((Commands.literal("explosion").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)))
        .then(Commands.argument(causingEntityString, EntityArgument.entity())
        .then(Commands.argument(posString, Vec3Argument.vec3())
        .then(Commands.argument(powerString, FloatArgumentType.floatArg(0.1F, 127.0F))
        .then(Commands.argument(createsFireString, BoolArgumentType.bool())
        .executes((commandContext) -> createExplosion(commandContext.getSource(), EntityArgument.getEntity(commandContext, causingEntityString),
        Vec3Argument.getVec3(commandContext, posString), FloatArgumentType.getFloat(commandContext, powerString), BoolArgumentType.getBool(commandContext, createsFireString))))))));
    }

    private static int createExplosion(CommandSourceStack commandSourceStack, Entity causingEntity, Vec3 position, float power, boolean createsFire) {

        commandSourceStack.getLevel().explode(causingEntity, Explosion.getDefaultDamageSource(commandSourceStack.getLevel(), causingEntity), null, position.x(), position.y(), position.z(), power, createsFire, Level.ExplosionInteraction.TNT);

        if (commandSourceStack.getPlayer() != null) {
            Component successText = Component.translatable("gui.chrysalis.commands.explosion_success", causingEntity.getName().getString(), power);
            commandSourceStack.getPlayer().sendSystemMessage(successText);
        }

        return 1;
    }
}