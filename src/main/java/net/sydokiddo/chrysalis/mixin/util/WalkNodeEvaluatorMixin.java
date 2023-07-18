package net.sydokiddo.chrysalis.mixin.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public class WalkNodeEvaluatorMixin {

    /**
     * Adds any blocks in the entities_should_walk_around tag to the same pathfinding malus that fences use.
     **/

    @Inject(method = "getBlockPathTypeRaw", at = @At("RETURN"), cancellable = true)
    private static void chrysalis_addPathfindingMalusBlocks(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<BlockPathTypes> cir) {

        BlockState blockState = blockGetter.getBlockState(blockPos);

        if (blockState.is(ChrysalisTags.ENTITIES_SHOULD_WALK_AROUND)) {
            cir.setReturnValue(BlockPathTypes.FENCE);
        }
    }
}