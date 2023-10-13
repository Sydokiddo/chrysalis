package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlockMixin {

    /**
     * Prevents any blocks in the cannot_be_pushed_by_pistons tag from being pushed by Pistons
     **/

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private static void chrysalis$preventPistonPushing(BlockState blockState, Level level, BlockPos blockPos, Direction direction, boolean bl, Direction direction2, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.is(ChrysalisTags.CANNOT_BE_PUSHED_BY_PISTONS)) {
            cir.setReturnValue(false);
        }
    }
}