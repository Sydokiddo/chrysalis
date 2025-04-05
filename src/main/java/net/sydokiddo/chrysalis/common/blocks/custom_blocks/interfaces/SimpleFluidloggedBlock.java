package net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.sydokiddo.chrysalis.common.blocks.CBlockStateProperties;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public interface SimpleFluidloggedBlock extends SimpleWaterloggedBlock {

    ImmutableMap.Builder<Fluid, Fluidlogged> STATE_FROM_FLUID = ImmutableMap.<Fluid, Fluidlogged>builder()
        .put(Fluids.EMPTY, Fluidlogged.AIR)
        .put(Fluids.WATER, Fluidlogged.WATER)
        .put(Fluids.LAVA, Fluidlogged.LAVA)
    ;

    static ImmutableMap<Fluid, Fluidlogged> stateFromFluidBuilder() {
        return STATE_FROM_FLUID.build();
    }

    private Fluid getFluid() {
        return stateFromFluidBuilder().entrySet().iterator().next().getKey();
    }

    private Fluidlogged getFluidloggedState() {
        return stateFromFluidBuilder().entrySet().iterator().next().getValue();
    }

    // region Block State Properties

    static BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext, BlockState defaultBlockState) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        if (fluidState.getType() == Fluids.WATER) return defaultBlockState.setValue(CBlockStateProperties.FLUIDLOGGED, Fluidlogged.WATER);
        else if (fluidState.getType() == Fluids.LAVA) return defaultBlockState.setValue(CBlockStateProperties.FLUIDLOGGED, Fluidlogged.LAVA);
        else return defaultBlockState;
    }

    static FluidState getFluidState(BlockState blockState, FluidState defaultFluidState) {
        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == Fluidlogged.WATER) return Fluids.WATER.getSource(false);
        else if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == Fluidlogged.LAVA) return Fluids.LAVA.getSource(false);
        else return defaultFluidState;
    }

    static BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos) {
        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == Fluidlogged.WATER) scheduledTickAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelReader));
        return blockState;
    }

    static int getLightEmission(BlockState blockState, int defaultLightLevel) {
        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == Fluidlogged.LAVA) return 15;
        return defaultLightLevel;
    }

    // endregion

    // region Fluid Interactions

    @Override
    default boolean canPlaceLiquid(@Nullable Player player, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull Fluid fluid) {
        EnumProperty<Fluidlogged> fluidloggedProperty = CBlockStateProperties.FLUIDLOGGED;
        return blockState.getValue(fluidloggedProperty) == Fluidlogged.AIR || blockState.getValue(fluidloggedProperty) == Fluidlogged.WATER && fluid == Fluids.WATER || blockState.getValue(fluidloggedProperty) == Fluidlogged.LAVA && fluid == Fluids.LAVA;
    }

    @Override
    default boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull FluidState fluidState) {

        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) != Fluidlogged.AIR) return false;

        if (!level.isClientSide()) {
            if (fluidState.getType() == Fluids.WATER) level.setBlock(blockPos, blockState.setValue(CBlockStateProperties.FLUIDLOGGED, Fluidlogged.WATER), 3);
            else if (fluidState.getType() == Fluids.LAVA) level.setBlock(blockPos, blockState.setValue(CBlockStateProperties.FLUIDLOGGED, Fluidlogged.LAVA), 3);
            level.scheduleTick(blockPos, fluidState.getType(), fluidState.getType().getTickDelay(level));
        }

        return true;
    }

    @Override
    default @NotNull ItemStack pickupBlock(@Nullable Player player, @NotNull LevelAccessor level, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {

        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == Fluidlogged.AIR) return ItemStack.EMPTY;

        level.setBlock(blockPos, blockState.setValue(CBlockStateProperties.FLUIDLOGGED, Fluidlogged.AIR), 3);
        if (!blockState.canSurvive(level, blockPos)) level.destroyBlock(blockPos, true);

        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == Fluidlogged.WATER) return new ItemStack(Items.WATER_BUCKET);
        else if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == Fluidlogged.LAVA) return new ItemStack(Items.LAVA_BUCKET);
        else return ItemStack.EMPTY;
    }

    // endregion
}