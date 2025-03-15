package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.sydokiddo.chrysalis.common.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin {

    /**
     * Blocks that turtle eggs are able to hatch on is now driven by the turtle_eggs_can_hatch_on tag rather than the sand tag.
     **/

    @Inject(method = "isSand", at = @At("HEAD"), cancellable = true)
    private static void chrysalis$turtleEggsCanHatchOn(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(blockGetter.getBlockState(blockPos).is(ChrysalisTags.TURTLE_EGGS_CAN_HATCH_ON));
    }
}