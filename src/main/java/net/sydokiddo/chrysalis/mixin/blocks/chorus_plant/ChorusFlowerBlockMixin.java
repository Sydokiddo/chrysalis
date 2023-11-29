package net.sydokiddo.chrysalis.mixin.blocks.chorus_plant;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin extends Block {

    @Shadow protected abstract void placeGrownFlower(Level level, BlockPos blockPos, int i);
    @Shadow @Final private Block plant;

    private ChorusFlowerBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * Chorus Flowers are now placeable on and tick on any blocks in the chorus_plant_can_grow_on tag.
     **/

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void chrysalis$chorusFlowerCanBePlacedOn(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
        if (levelReader.getBlockState(blockPos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void chrysalis$chorusFlowerRandomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo info) {

        BlockPos abovePos = blockPos.above();
        int age = blockState.getValue(ChorusFlowerBlock.AGE);

        if (serverLevel.getBlockState(blockPos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON) && serverLevel.isEmptyBlock(abovePos) && abovePos.getY() < serverLevel.getMaxBuildHeight() && age < 5) {
            this.placeGrownFlower(serverLevel, abovePos, age + 1);
            serverLevel.setBlock(blockPos, this.plant.defaultBlockState().setValue(ChorusPlantBlock.UP, true).setValue(ChorusPlantBlock.DOWN, true), 2);
            info.cancel();
        }
    }

    @Inject(method = "placeDeadFlower", at = @At("HEAD"), cancellable = true)
    private void chrysalis$placeDeadChorusFlower(Level level, BlockPos blockPos, CallbackInfo info) {

        BlockState belowPos = level.getBlockState(blockPos.below());

        if (belowPos.is(Blocks.CHORUS_PLANT) || belowPos.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            level.setBlock(blockPos, this.defaultBlockState().setValue(BlockStateProperties.AGE_5, 5), 2);
            level.levelEvent(1034, blockPos, 0);
        }
        info.cancel();
    }
}