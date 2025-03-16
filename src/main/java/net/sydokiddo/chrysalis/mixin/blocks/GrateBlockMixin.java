package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.common.misc.ChrysalisTags;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HalfTransparentBlock.class)
public class GrateBlockMixin extends Block {

    private GrateBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * Occludes blocked faces of any grate block when adjacent to each other.
     **/

    @Inject(at = @At("HEAD"), method = "skipRendering", cancellable = true)
    private void chrysalis$occludeGrateFaces(BlockState blockState, BlockState adjacentBlock, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        TagKey<Block> grates = ChrysalisTags.GRATES;
        if (blockState.is(grates) && CConfigOptions.IMPROVED_GRATE_RENDERING.get()) cir.setReturnValue(adjacentBlock.is(grates) || super.skipRendering(blockState, adjacentBlock, direction));
    }
}