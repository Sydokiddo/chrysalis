package net.junebug.chrysalis.util.technical.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.junebug.chrysalis.util.technical.commands.util.CommandCommonMethods;
import java.util.Objects;

public class SurfaceCommand {

    /**
     * A command to teleport the player to the highest possible solid block position.
     **/

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

        Component successText = Component.translatable("gui.chrysalis.commands.surface.success", serverPlayer.getDisplayName());
        Component failText = Component.translatable("gui.chrysalis.commands.surface.fail").withStyle(ChatFormatting.RED);

        if (highestYValue > y && !serverPlayer.level().getBlockState(new BlockPos(x, highestYValue, z).below()).isAir()) {
            serverPlayer.teleportTo(playerPos.getX(), highestYValue, playerPos.getZ());
            CommandCommonMethods.sendFeedbackMessage(true, serverPlayer, successText);
        } else {
            CommandCommonMethods.sendFeedbackMessage(false, serverPlayer, failText);
        }

        return 1;
    }
}