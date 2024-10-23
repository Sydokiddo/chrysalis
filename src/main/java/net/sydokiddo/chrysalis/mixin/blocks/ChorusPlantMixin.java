package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ChorusPlantFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantBlock.class)
public abstract class ChorusPlantMixin extends PipeBlock {

    private ChorusPlantMixin(float shapeByIndex, Properties properties) {
        super(shapeByIndex, properties);
    }

    /**
     * Chorus Plants are now placeable on any blocks in the chorus_plant_can_grow_on tag.
     **/

    @Inject(method = "getStateWithConnections", at = @At("HEAD"), cancellable = true)
    private static void chrysalis$chorusPlantGetStateWithConnections(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<BlockState> cir) {

        BlockState belowState = blockGetter.getBlockState(blockPos.below());
        BlockState aboveState = blockGetter.getBlockState(blockPos.above());

        BlockState northState = blockGetter.getBlockState(blockPos.north());
        BlockState eastState = blockGetter.getBlockState(blockPos.east());
        BlockState southState = blockGetter.getBlockState(blockPos.south());
        BlockState westState = blockGetter.getBlockState(blockPos.west());

        Block block = blockState.getBlock();
        Block chorusFlower = Blocks.CHORUS_FLOWER;

        cir.setReturnValue(blockState.trySetValue(DOWN, belowState.is(block) || belowState.is(chorusFlower) || belowState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON))
        .trySetValue(UP, aboveState.is(block) || aboveState.is(chorusFlower))
        .trySetValue(NORTH, northState.is(block) || northState.is(chorusFlower))
        .trySetValue(EAST, eastState.is(block) || eastState.is(chorusFlower))
        .trySetValue(SOUTH, southState.is(block) || southState.is(chorusFlower))
        .trySetValue(WEST, westState.is(block) || westState.is(chorusFlower)));
    }

    @Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
    private void chrysalis$chorusPlantUpdateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos adjacentPos, BlockState adjacentState, RandomSource randomSource, CallbackInfoReturnable<BlockState> cir) {
        if (blockState.canSurvive(levelReader, blockPos)) {
            boolean canGrowFrom = adjacentState.is(this) || adjacentState.is(Blocks.CHORUS_FLOWER) || direction == Direction.DOWN && adjacentState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
            cir.setReturnValue(blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), canGrowFrom));
        }
    }

    /**
     * @author Sydokiddo
     * @reason Overrides the base canSurvive method and allows for Chorus Plants to grow on the chorus_plant_can_grow_on tag.
     */

    @Overwrite
    public boolean canSurvive(@NotNull BlockState blockState, LevelReader levelReader, BlockPos blockPos) {

        BlockState belowBlockState = levelReader.getBlockState(blockPos.below());

        for (Direction direction : Direction.Plane.HORIZONTAL) {

            BlockPos relativeBlockPos = blockPos.relative(direction);
            if (!levelReader.getBlockState(relativeBlockPos).is(this)) continue;

            if (!levelReader.getBlockState(blockPos.above()).isAir() && !belowBlockState.isAir()) return false;

            BlockState belowRelative = levelReader.getBlockState(relativeBlockPos.below());
            if (!belowRelative.is(this) && !belowRelative.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) continue;

            return true;
        }

        return belowBlockState.is(this) || belowBlockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }

    @SuppressWarnings("unused")
    @Mixin(ChorusFlowerBlock.class)
    public static class ChorusFlowerBlockMixin {

        /**
         * Chorus Flowers are now placeable on and tick on any blocks in the chorus_plant_can_grow_on tag.
         **/

        @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
        private void chrysalis$chorusFlowerCanBePlacedOn(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
            if (levelReader.getBlockState(blockPos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
                cir.setReturnValue(true);
                cir.cancel();
            }
        }

        @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
        private boolean chrysalis$chorusFlowerRandomTick1(BlockState blockState, Block block) {
            return blockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
        }

        @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 3))
        private boolean chrysalis$chorusFlowerRandomTick2(BlockState blockState, Block block) {
            return blockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
        }
    }

    @SuppressWarnings("unused")
    @Mixin(ChorusPlantFeature.class)
    public static class ChorusPlantFeatureMixin {

        /**
         * Allows for the Chorus Plant configured feature to be placed on any blocks in the chorus_plant_can_grow_on tag.
         **/

        @Inject(method = "place", at = @At("RETURN"), cancellable = true)
        private void chrysalis$placeChorusPlantFeature(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> cir) {

            WorldGenLevel worldGenLevel = featurePlaceContext.level();
            BlockPos blockPos = featurePlaceContext.origin();

            if (worldGenLevel.isEmptyBlock(blockPos) && worldGenLevel.getBlockState(blockPos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
                ChorusFlowerBlock.generatePlant(worldGenLevel, blockPos, featurePlaceContext.random(), 8);
                cir.setReturnValue(true);
            }
        }
    }
}