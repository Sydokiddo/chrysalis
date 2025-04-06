package net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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
import java.util.Optional;

public interface SimpleFluidloggedBlock extends SimpleWaterloggedBlock {

    /**
     * Any block that integrates this interface can be fluidlogged with any of the given fluid types. More states can be added easily using mixins.
     **/

    // region Fluid Maps

    Supplier<ImmutableBiMap<Fluid, Fluidlogged>> STATE_FROM_FLUID = Suppliers.memoize(
        () -> ImmutableBiMap.<Fluid, Fluidlogged>builder()
            .put(Fluids.EMPTY, Fluidlogged.AIR)
            .put(Fluids.WATER, Fluidlogged.WATER)
            .put(Fluids.LAVA, Fluidlogged.LAVA)
        .build()
    );

    Supplier<ImmutableBiMap<Fluidlogged, Fluid>> FLUID_FROM_STATE = Suppliers.memoize(
        () -> STATE_FROM_FLUID.get().inverse()
    );

    private static Fluidlogged getStateFromFluid(Fluid fluid) {
        return STATE_FROM_FLUID.get().get(fluid);
    }

    private static Fluid getFluidFromState(Fluidlogged fluidlogged) {
        return FLUID_FROM_STATE.get().get(fluidlogged);
    }

    // endregion

    // region Block State Properties

    static BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext, BlockState defaultBlockState) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        if (fluidState.getType() == getFluidFromState(getStateFromFluid(fluidState.getType()))) return defaultBlockState.setValue(CBlockStateProperties.FLUIDLOGGED, getStateFromFluid(fluidState.getType()));
        else return defaultBlockState;
    }

    static FluidState getFluidState(BlockState blockState, FluidState defaultFluidState) {
        Fluidlogged fluidlogged = blockState.getValue(CBlockStateProperties.FLUIDLOGGED);
        Fluid fluidFromState = getFluidFromState(fluidlogged);
        if (fluidlogged != Fluidlogged.AIR && fluidlogged == getStateFromFluid(fluidFromState)) return fluidFromState.defaultFluidState().setValue(FlowingFluid.FALLING, false);
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
        EnumProperty<Fluidlogged> fluidloggedProperty = CBlockStateProperties.FLUIDLOGGED;
        return (blockState.getValue(fluidloggedProperty) == Fluidlogged.AIR || blockState.getValue(fluidloggedProperty) == getStateFromFluid(fluid) && fluid == getFluidFromState(getStateFromFluid(fluid)));
    }

    @Override
    default boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull FluidState fluidState) {

        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) != Fluidlogged.AIR) return false;

        if (!level.isClientSide()) {
            if (fluidState.getType() == getFluidFromState(getStateFromFluid(fluidState.getType()))) level.setBlock(blockPos, blockState.setValue(CBlockStateProperties.FLUIDLOGGED, getStateFromFluid(fluidState.getType())), 3);
            level.scheduleTick(blockPos, fluidState.getType(), fluidState.getType().getTickDelay(level));
        }

        return true;
    }

    @Override
    default @NotNull ItemStack pickupBlock(@Nullable Player player, @NotNull LevelAccessor level, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {

        Fluidlogged fluidlogged = blockState.getValue(CBlockStateProperties.FLUIDLOGGED);
        if (fluidlogged == Fluidlogged.AIR) return ItemStack.EMPTY;

        level.setBlock(blockPos, blockState.setValue(CBlockStateProperties.FLUIDLOGGED, Fluidlogged.AIR), 3);
        if (!blockState.canSurvive(level, blockPos)) level.destroyBlock(blockPos, true);

        Fluid fluidFromState = getFluidFromState(fluidlogged);
        if (fluidlogged == getStateFromFluid(fluidFromState)) return new ItemStack(fluidFromState.getBucket());
        else return ItemStack.EMPTY;
    }

    @Override
    default @NotNull Optional<SoundEvent> getPickupSound(@NotNull BlockState blockState) {
        return getFluidFromState(blockState.getValue(CBlockStateProperties.FLUIDLOGGED)).getPickupSound();
    }

    // endregion
}