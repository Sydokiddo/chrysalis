package net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.sydokiddo.chrysalis.common.blocks.CBlockStateProperties;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface SimpleFluidloggedBlock extends SimpleWaterloggedBlock {

    /**
     * Any block that integrates this interface can be fluidlogged with any of the given fluid types. More states can be added easily using mixins.
     **/

    // region Fluid Maps

    Map<Fluid, FluidloggedState> stateFromFluidMap = new HashMap<>();

    Supplier<ImmutableBiMap<Fluid, FluidloggedState>> STATE_FROM_FLUID = Suppliers.memoize(
        () -> ImmutableBiMap.<Fluid, FluidloggedState>builder()
            .put(Fluids.EMPTY, FluidloggedState.AIR)
            .put(Fluids.WATER, FluidloggedState.WATER)
            .put(Fluids.LAVA, FluidloggedState.LAVA)
            .putAll(stateFromFluidMap.entrySet())
        .build()
    );

    Supplier<ImmutableBiMap<FluidloggedState, Fluid>> FLUID_FROM_STATE = Suppliers.memoize(
        () -> STATE_FROM_FLUID.get().inverse()
    );

    static FluidloggedState getStateFromFluid(Fluid fluid) {
        return STATE_FROM_FLUID.get().get(fluid);
    }

    static Fluid getFluidFromState(FluidloggedState fluidloggedState) {
        return FLUID_FROM_STATE.get().get(fluidloggedState);
    }

    @SuppressWarnings("unused")
    static FluidloggedState getStateAtPos(Level level, BlockPos blockPos) {
        return STATE_FROM_FLUID.get().getOrDefault(level.getFluidState(blockPos).getType(), FluidloggedState.AIR);
    }

    // endregion

    // region Block State Properties

    static BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext, BlockState defaultBlockState) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        if (fluidState.getType() == getFluidFromState(getStateFromFluid(fluidState.getType()))) return defaultBlockState.setValue(CBlockStateProperties.FLUIDLOGGED, getStateFromFluid(fluidState.getType()));
        else return defaultBlockState;
    }

    static FluidState getFluidState(BlockState blockState, FluidState defaultFluidState) {
        FluidloggedState fluidloggedState = blockState.getValue(CBlockStateProperties.FLUIDLOGGED);
        Fluid fluidFromState = getFluidFromState(fluidloggedState);
        if (fluidloggedState != FluidloggedState.AIR && fluidloggedState == getStateFromFluid(fluidFromState)) return fluidFromState.defaultFluidState().setValue(FlowingFluid.FALLING, false);
        else return defaultFluidState;
    }

    static BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos) {
        FluidState fluidState = levelReader.getFluidState(blockPos);
        Fluid fluidFromState = getFluidFromState(getStateFromFluid(fluidState.getType()));
        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == getStateFromFluid(fluidState.getType())) scheduledTickAccess.scheduleTick(blockPos, fluidFromState, fluidFromState.getTickDelay(levelReader));
        return blockState;
    }

    static int getLightEmission(BlockState blockState) {
        return getFluidFromState(blockState.getValue(CBlockStateProperties.FLUIDLOGGED)).getFluidType().getLightLevel();
    }

    // endregion

    // region Fluid Interactions

    @Override
    default boolean canPlaceLiquid(@Nullable Player player, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull Fluid fluid) {
        return tryCanPlaceLiquid(blockState, fluid);
    }

    static boolean tryCanPlaceLiquid(BlockState blockState, @NotNull Fluid fluid) {
        EnumProperty<FluidloggedState> fluidloggedState = CBlockStateProperties.FLUIDLOGGED;
        return (blockState.getValue(fluidloggedState) == FluidloggedState.AIR || blockState.getValue(fluidloggedState) == getStateFromFluid(fluid) && fluid == getFluidFromState(getStateFromFluid(fluid)));
    }

    @Override
    default boolean placeLiquid(@NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull FluidState fluidState) {
        return tryPlaceLiquid(levelAccessor, blockPos, blockState, fluidState);
    }

    static boolean tryPlaceLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {

        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) != FluidloggedState.AIR) return false;

        if (!levelAccessor.isClientSide()) {
            if (fluidState.getType() == getFluidFromState(getStateFromFluid(fluidState.getType()))) levelAccessor.setBlock(blockPos, blockState.setValue(CBlockStateProperties.FLUIDLOGGED, getStateFromFluid(fluidState.getType())), 3);
            levelAccessor.scheduleTick(blockPos, fluidState.getType(), fluidState.getType().getTickDelay(levelAccessor));
        }

        return true;
    }

    @Override
    default @NotNull ItemStack pickupBlock(@Nullable Player player, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return tryPickupBlock(levelAccessor, blockPos, blockState);
    }

    static ItemStack tryPickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {

        FluidloggedState fluidloggedState = blockState.getValue(CBlockStateProperties.FLUIDLOGGED);
        if (fluidloggedState == FluidloggedState.AIR) return ItemStack.EMPTY;

        levelAccessor.setBlock(blockPos, blockState.setValue(CBlockStateProperties.FLUIDLOGGED, FluidloggedState.AIR), 3);
        if (!blockState.canSurvive(levelAccessor, blockPos)) levelAccessor.destroyBlock(blockPos, true);

        Fluid fluidFromState = getFluidFromState(fluidloggedState);
        if (fluidloggedState == getStateFromFluid(fluidFromState)) return new ItemStack(fluidFromState.getBucket());
        else return ItemStack.EMPTY;
    }

    @Override
    default @NotNull Optional<SoundEvent> getPickupSound(@NotNull BlockState blockState) {
        return tryGetPickupSound(blockState);
    }

    static Optional<SoundEvent> tryGetPickupSound(BlockState blockState) {
        return getFluidFromState(blockState.getValue(CBlockStateProperties.FLUIDLOGGED)).getPickupSound();
    }

    // endregion
}