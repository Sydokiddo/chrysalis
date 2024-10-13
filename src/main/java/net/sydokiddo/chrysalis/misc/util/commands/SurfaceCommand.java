package net.sydokiddo.chrysalis.misc.util.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Objects;

public class SurfaceCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("surface").requires((commandSourceStack) -> commandSourceStack.hasPermission(2)).executes(SurfaceCommand::teleportPlayerToSurface));
    }

    private static int teleportPlayerToSurface(CommandContext<CommandSourceStack> context) {

        Player player = Objects.requireNonNull(context.getSource().getPlayer());
        if (player.isPassenger()) player.stopRiding();

        int x = player.getBlockX();
        int y = player.getBlockY();
        int z = player.getBlockZ();
        BlockPos playerPos = player.getOnPos();

        int highestYValue = player.level().getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        BlockPos highestBlockPos = new BlockPos(x, highestYValue, z);
        BlockState highestBlockState = player.level().getBlockState(highestBlockPos.below());

        Component successText = Component.translatable("gui.chrysalis.commands.surface.success", player.getDisplayName());
        Component failText = Component.translatable("gui.chrysalis.commands.surface.fail").withStyle(ChatFormatting.RED);

        if (highestYValue != y && !highestBlockState.isAir() && !highestBlockState.getCollisionShape(player.level(), highestBlockPos).isEmpty()) {
            player.teleportTo(playerPos.getX(), highestYValue, playerPos.getZ());
            player.sendSystemMessage(successText);
        } else {
            player.sendSystemMessage(failText);
        }

        return 1;
    }
}