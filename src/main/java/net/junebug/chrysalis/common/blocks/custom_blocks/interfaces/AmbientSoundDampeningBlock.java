package net.junebug.chrysalis.common.blocks.custom_blocks.interfaces;

import net.junebug.chrysalis.common.misc.CTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

@SuppressWarnings("unused")
public interface AmbientSoundDampeningBlock {

    /**
     * An interface for detecting ambient sound dampening blocks that are adjacent to a given block position, and whether a block can emit ambient sounds.
     **/

    static boolean canEmitAmbientSounds(BlockGetter blockGetter, BlockPos blockPos) {
        for (Direction direction : Direction.values()) if (blockGetter.getBlockState(blockPos.relative(direction)).is(CTags.DAMPENS_BLOCK_AMBIENT_SOUNDS)) return false;
        return true;
    }
}