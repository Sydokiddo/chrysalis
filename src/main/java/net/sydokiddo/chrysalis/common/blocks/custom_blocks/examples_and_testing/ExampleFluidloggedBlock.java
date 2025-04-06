package net.sydokiddo.chrysalis.common.blocks.custom_blocks.examples_and_testing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.sydokiddo.chrysalis.common.blocks.CBlockStateProperties;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces.SimpleFluidloggedBlock;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class ExampleFluidloggedBlock extends Block implements SimpleFluidloggedBlock {

    /**
     * An example class to show how to set up a fluidloggable block.
     **/

    public ExampleFluidloggedBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CBlockStateProperties.FLUIDLOGGED, FluidloggedState.AIR));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CBlockStateProperties.FLUIDLOGGED);
    }

    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return SimpleFluidloggedBlock.getStateForPlacement(blockPlaceContext, this.defaultBlockState());
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState blockState) {
        return SimpleFluidloggedBlock.getFluidState(blockState, super.getFluidState(blockState));
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockPos oldPos, @NotNull BlockState oldState, @NotNull RandomSource randomSource) {
        return SimpleFluidloggedBlock.updateShape(blockState, levelReader, scheduledTickAccess, blockPos);
    }

    @Override
    public int getLightEmission(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return SimpleFluidloggedBlock.getLightEmission(blockState);
    }
}