package net.junebug.chrysalis.common.blocks.custom_blocks;

import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.SimpleFluidloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.Iterator;

public class FluidloggedMultifaceBlock extends MultifaceBlock implements SimpleFluidloggedBlock {

    /**
     * A version of the MultifaceBlock class that allows for the block to be fluidlogged instead of exclusively waterlogged.
     **/

    // region Initialization

    public FluidloggedMultifaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(getDefaultMultifaceState(this.getStateDefinition()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CBlockStateProperties.FLUIDLOGGED));
    }

    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockState currentState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull Direction lookingDirection) {

        if (!this.isValidStateForPlacement(blockGetter, currentState, blockPos, lookingDirection)) {
            return null;
        } else {
            BlockState blockState;
            if (currentState.is(this)) blockState = currentState;
            else blockState = this.defaultBlockState().setValue(CBlockStateProperties.FLUIDLOGGED, SimpleFluidloggedBlock.getStateFromFluid(currentState.getFluidState().getType()));
            return blockState.setValue(getFaceProperty(lookingDirection), true);
        }
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState blockState) {
        return SimpleFluidloggedBlock.getFluidState(blockState, super.getFluidState(blockState));
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockPos adjacentPos, @NotNull BlockState adjacentState, @NotNull RandomSource randomSource) {
        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) != FluidloggedState.AIR) SimpleFluidloggedBlock.scheduleShapeTick(blockState, levelReader, scheduledTickAccess, blockPos);
        if (!hasAnyFace(blockState)) return this.getFluidState(blockState).createLegacyBlock();
        else return hasFace(blockState, direction) && !canAttachTo(levelReader, direction, adjacentPos, adjacentState) ? this.removeFace(blockState, getFaceProperty(direction)) : blockState;
    }

    @Override
    public int getLightEmission(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return SimpleFluidloggedBlock.getLightEmission(super.getLightEmission(blockState, blockGetter, blockPos), blockState);
    }

    public static @NotNull BlockState getDefaultMultifaceState(StateDefinition<Block, BlockState> stateDefinition) {
        BlockState blockState = stateDefinition.any().setValue(CBlockStateProperties.FLUIDLOGGED, FluidloggedState.AIR).setValue(BlockStateProperties.WATERLOGGED, false);
        BooleanProperty booleanProperty;
        for (Iterator<BooleanProperty> iterator = PipeBlock.PROPERTY_BY_DIRECTION.values().iterator(); iterator.hasNext(); blockState = blockState.trySetValue(booleanProperty, false)) booleanProperty = iterator.next();
        return blockState;
    }

    public BlockState removeFace(BlockState blockState, BooleanProperty faceProp) {
        BlockState faceState = blockState.setValue(faceProp, false);
        return hasAnyFace(faceState) ? faceState : Blocks.AIR.defaultBlockState();
    }

    // endregion
}