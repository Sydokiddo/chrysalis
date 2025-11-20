package net.junebug.chrysalis.common.blocks.custom_blocks.interfaces;

import net.junebug.chrysalis.common.misc.CTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public interface AmbientSoundEmittingBlock {

    /**
     * An interface for determining whether a block can emit ambient sounds.
     **/

    static boolean canEmitAmbientSounds(BlockGetter blockGetter, BlockPos blockPos) {
        for (Direction direction : Direction.values()) if (blockGetter.getBlockState(blockPos.relative(direction)).is(CTags.DAMPENS_BLOCK_AMBIENT_SOUNDS)) return false;
        return true;
    }

    static boolean canEmitAmbientSounds(BlockGetter blockGetter, BlockPos blockPos, Block block) {

        for (Direction direction : Direction.values()) {
            if (blockGetter.getBlockState(blockPos.relative(direction)).is(CTags.DAMPENS_BLOCK_AMBIENT_SOUNDS)) return false;
            return (blockGetter.getBlockState(blockPos.relative(direction)).is(block));
        }

        return false;
    }

    static boolean canEmitAmbientSounds(BlockGetter blockGetter, BlockPos blockPos, TagKey<Block> blockTag) {

        for (Direction direction : Direction.values()) {
            if (blockGetter.getBlockState(blockPos.relative(direction)).is(CTags.DAMPENS_BLOCK_AMBIENT_SOUNDS)) return false;
            return (blockGetter.getBlockState(blockPos.relative(direction)).is(blockTag));
        }

        return false;
    }

    static void playAmbientSound(Level level, BlockPos blockPos, RandomSource randomSource, SoundEvent soundEvent, boolean canEmitSounds, int interval) {
        if (canEmitSounds && randomSource.nextInt(interval) == 0) level.playLocalSound((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, soundEvent, SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F, false);
    }
}