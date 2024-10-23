package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Objects;

public class SurfaceCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("surface").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).executes(SurfaceCommand::teleportPlayerToSurface));
    }

    private static int teleportPlayerToSurface(CommandContext<CommandSourceStack> context) {

        ServerPlayer serverPlayer = Objects.requireNonNull(context.getSource().getPlayer());
        if (serverPlayer.isPassenger()) serverPlayer.stopRiding();

        int x = serverPlayer.getBlockX();
        int y = serverPlayer.getBlockY();
        int z = serverPlayer.getBlockZ();
        BlockPos playerPos = serverPlayer.getOnPos();

        int highestYValue = serverPlayer.level().getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        BlockPos highestBlockPos = new BlockPos(x, highestYValue, z);
        BlockState highestBlockState = serverPlayer.level().getBlockState(highestBlockPos.below());

        Component successText = Component.translatable("gui.chrysalis.commands.surface.success", serverPlayer.getDisplayName());
        Component failText = Component.translatable("gui.chrysalis.commands.surface.fail").withStyle(ChatFormatting.RED);

        if (highestYValue != y && !highestBlockState.isAir() && (!highestBlockState.getFluidState().isEmpty() || !highestBlockState.getCollisionShape(serverPlayer.level(), highestBlockPos).isEmpty())) {
            serverPlayer.teleportTo(playerPos.getX(), highestYValue, playerPos.getZ());
            serverPlayer.sendSystemMessage(successText);
        } else {
            serverPlayer.sendSystemMessage(failText);
        }

        return 1;
    }
}