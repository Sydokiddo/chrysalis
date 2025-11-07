package net.junebug.chrysalis.common.blocks.custom_blocks;

import com.mojang.serialization.MapCodec;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.SimpleFluidloggedBlock;
import net.junebug.chrysalis.common.entities.custom_entities.PlaceholderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class PlaceholderBlock extends BaseEntityBlock implements SimpleFluidloggedBlock {

    /**
     * The class for placeholder blocks, a block that replaces itself with a given block id once loaded/powered.
     **/

    // region Initialization

    public PlaceholderBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE, PlaceholderBlockModelState.FULL_BLOCK).setValue(CBlockStateProperties.FLUIDLOGGED, FluidloggedState.AIR));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(PlaceholderBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new PlaceholderBlockEntity(blockPos, blockState);
    }

    // endregion

    // region Block States

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE, CBlockStateProperties.FLUIDLOGGED);
    }

    @Nullable @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return SimpleFluidloggedBlock.getStateForPlacement(blockPlaceContext, this.defaultBlockState());
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        if (blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlockModelState.FULL_BLOCK) super.getShape(blockState, blockGetter, blockPos, collisionContext);
        return Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        if (blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlockModelState.BILLBOARD) return RenderShape.INVISIBLE;
        return super.getRenderShape(blockState);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        if (blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlockModelState.FULL_BLOCK) return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
        return Shapes.empty();
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockPos oldPos, @NotNull BlockState oldState, @NotNull RandomSource randomSource) {
        return SimpleFluidloggedBlock.updateShape(blockState, levelReader, scheduledTickAccess, blockPos);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState blockState) {
        return SimpleFluidloggedBlock.getFluidState(blockState, Fluids.EMPTY.defaultFluidState());
    }

    @Override
    public int getLightEmission(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return SimpleFluidloggedBlock.getLightEmission(blockState);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState blockState) {
        if (blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlockModelState.FULL_BLOCK) return super.propagatesSkylightDown(blockState);
        return blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == FluidloggedState.AIR;
    }

    // endregion

    public enum PlaceholderBlockModelState implements StringRepresentable {

        FULL_BLOCK("full_block"),
        CROSS("cross"),
        BILLBOARD("billboard");

        private final String stateName;

        PlaceholderBlockModelState(String stateName) {
            this.stateName = stateName;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.stateName;
        }
    }
}