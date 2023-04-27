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
import net.sydokiddo.chrysalis.misc.util.ModBlocksHelper;
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
    @Final @Shadow private ChorusPlantBlock plant;

    public ChorusFlowerBlockMixin(Properties properties) {
        super(properties);
    }

    // Chorus Flowers can be placed on any block in the chorus_plant_can_grow_on tag

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void chrysalis_chorusFlowerCanBePlacedOn(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (world.getBlockState(pos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void chrysalis_chorusFlowerRandomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo info) {
        if (world.getBlockState(pos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            BlockPos up = pos.above();
            if (world.isEmptyBlock(up) && up.getY() < world.getMaxBuildHeight()) {
                int i = state.getValue(ChorusFlowerBlock.AGE);
                if (i < 5) {
                    this.placeGrownFlower(world, up, i + 1);
                    ModBlocksHelper.setWithoutUpdate(world, pos, plant.defaultBlockState().setValue(ChorusPlantBlock.UP, true).setValue(ChorusPlantBlock.DOWN, true));
                    info.cancel();
                }
            }
        }
    }

    @Inject(method = "placeDeadFlower", at = @At("HEAD"), cancellable = true)
    private void chrysalis_placeDeadChorusFlower(Level world, BlockPos pos, CallbackInfo info) {
        BlockState belowPos = world.getBlockState(pos.below());
        if (belowPos.is(Blocks.CHORUS_PLANT) || belowPos.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            world.setBlock(pos, this.defaultBlockState().setValue(BlockStateProperties.AGE_5, 5), 2);
            world.levelEvent(1034, pos, 0);
        }
        info.cancel();
    }
}
