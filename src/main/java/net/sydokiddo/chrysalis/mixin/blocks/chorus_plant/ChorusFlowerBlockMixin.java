package net.sydokiddo.chrysalis.mixin.blocks.chorus_plant;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerBlockMixin {

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