package net.sydokiddo.chrysalis.mixin.blocks.chorus_plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantBlock.class)
public class ChorusPlantBlockMixin extends PipeBlock {

    private ChorusPlantBlockMixin(float f, Properties properties) {
        super(f, properties);
    }

    /**
     * Chorus Plants are now able to be placed on any blocks in the chorus_plant_can_grow_on tag.
     **/

    @Inject(method = "getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"), cancellable = true)
    private void chrysalis$chorusPlantGetStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> info) {

        BlockState plant = info.getReturnValue();

        if (context.canPlace() && plant.is(Blocks.CHORUS_PLANT) && context.getLevel().getBlockState(context.getClickedPos().below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            info.setReturnValue(plant.setValue(BlockStateProperties.DOWN, true));
        }
    }

    @Inject(method = "Lnet/minecraft/world/level/block/ChorusPlantBlock;getStateForPlacement" + "(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)" + "Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"), cancellable = true)
    private void chrysalis$chorusPlantGetStateForPlacement(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<BlockState> info) {

        BlockState plant = info.getReturnValue();

        if (plant.is(Blocks.CHORUS_PLANT) && blockGetter.getBlockState(blockPos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            info.setReturnValue(plant.setValue(BlockStateProperties.DOWN, true));
        }
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void chrysalis$chorusPlantCanSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (levelReader.getBlockState(blockPos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
    private void chrysalis$chorusPlantUpdateShape(BlockState currentState, Direction direction, BlockState newState, LevelAccessor levelAccessor, BlockPos pos, BlockPos posFrom, CallbackInfoReturnable<BlockState> info) {

        BlockState plant = info.getReturnValue();

        if (plant.is(Blocks.CHORUS_PLANT) && levelAccessor.getBlockState(pos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            plant = plant.setValue(BlockStateProperties.DOWN, true);
            info.setReturnValue(plant);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(@NotNull BlockState blockState, LevelReader levelReader, BlockPos blockPos) {

        BlockState belowBlockState = levelReader.getBlockState(blockPos.below());

        for (Direction direction : Direction.Plane.HORIZONTAL) {

            BlockPos relativeBlockPos = blockPos.relative(direction);
            if (!levelReader.getBlockState(relativeBlockPos).is(this)) continue;

            if (!levelReader.getBlockState(blockPos.above()).isAir() && !belowBlockState.isAir()) {
                return false;
            }

            BlockState belowRelative = levelReader.getBlockState(relativeBlockPos.below());
            if (!belowRelative.is(this) && !belowRelative.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) continue;

            return true;
        }
        return belowBlockState.is(this) || belowBlockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }
}