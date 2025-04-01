package net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.common.blocks.CBlockStateProperties;

public interface DissipatingBlockInterface {

    default void scheduleTick(ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Block block, int delay) {
        scheduledTickAccess.scheduleTick(blockPos, block, delay);
    }

    default void dissipateBlock(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, boolean dropBlock) {
        serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(CBlockStateProperties.DESTROYED, Boolean.TRUE));
        serverLevel.destroyBlock(blockPos, dropBlock);
    }
}