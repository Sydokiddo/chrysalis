package net.sydokiddo.chrysalis.mixin.blocks.chorus_plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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

    public ChorusPlantBlockMixin(float f, Properties properties) {
        super(f, properties);
    }

    // Chorus Plants can be placed on any block in the chorus_plant_can_grow_on tag

    @Inject(method = "getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"), cancellable = true)
    private void chrysalis_chorusPlantCanGrowOn(BlockPlaceContext context, CallbackInfoReturnable<BlockState> info) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState plant = info.getReturnValue();
        if (context.canPlace() && plant.is(Blocks.CHORUS_PLANT) && world.getBlockState(pos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            info.setReturnValue(plant.setValue(BlockStateProperties.DOWN, true));
        }
    }

    @Inject(method = "Lnet/minecraft/world/level/block/ChorusPlantBlock;getStateForPlacement" + "(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)" + "Lnet/minecraft/world/level/block/state/BlockState;", at = @At("RETURN"), cancellable = true)
    private void chrysalis_chorusPlantCanGrowOn(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<BlockState> info) {
        BlockState plant = info.getReturnValue();
        if (plant.is(Blocks.CHORUS_PLANT) && blockGetter.getBlockState(blockPos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            info.setReturnValue(plant.setValue(BlockStateProperties.DOWN, true));
        }
    }

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void chrysalis_chorusPlantCanGrowOn(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BlockState down = world.getBlockState(pos.below());
        if (down.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
    private void chrysalis_chorusPlantCanGrowOn(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom, CallbackInfoReturnable<BlockState> info) {
        BlockState plant = info.getReturnValue();
        if (plant.is(Blocks.CHORUS_PLANT) && world.getBlockState(pos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            plant = plant.setValue(BlockStateProperties.DOWN, true);
            info.setReturnValue(plant);
        }
    }

    @SuppressWarnings("ALL")
    @Override
    public boolean canSurvive(@NotNull BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockState blockState2 = levelReader.getBlockState(blockPos.below());
        boolean bl = !levelReader.getBlockState(blockPos.above()).isAir() && !blockState2.isAir();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockPos2 = blockPos.relative(direction);
            BlockState blockState3 = levelReader.getBlockState(blockPos2);
            if (!blockState3.is(this)) continue;
            if (bl) {
                return false;
            }
            BlockState blockState4 = levelReader.getBlockState(blockPos2.below());
            if (!blockState4.is(this) && !blockState4.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) continue;
            return true;
        }
        return blockState2.is(this) || blockState2.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }
}
