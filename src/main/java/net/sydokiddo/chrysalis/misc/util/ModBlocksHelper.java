package net.sydokiddo.chrysalis.misc.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class ModBlocksHelper {

    /**
     * Assists with placing specific types of blocks without them triggering observers.
     * Currently, this only used by Chorus Flower random ticking.
     **/

    private static final int FLAG_SEND_CLIENT_CHANGES = 2;
    private static final int FLAG_IGNORE_OBSERVERS = 16;
    private static final int SET_SILENT = FLAG_IGNORE_OBSERVERS | FLAG_SEND_CLIENT_CHANGES;

    public static void setWithoutUpdate(LevelAccessor world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, SET_SILENT);
    }
}