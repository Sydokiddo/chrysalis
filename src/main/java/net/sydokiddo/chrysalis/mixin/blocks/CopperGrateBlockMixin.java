package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HalfTransparentBlock.class)
public class CopperGrateBlockMixin extends Block {

    private CopperGrateBlockMixin(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Inject(at = @At("HEAD"), method = "skipRendering", cancellable = true)
    private void chrysalis$occludeCopperGrateFaces(BlockState blockState, BlockState adjacentBlock, Direction direction, CallbackInfoReturnable<Boolean> cir) {

        TagKey<Block> copperGrates = ChrysalisTags.COPPER_GRATES;

        if (blockState.is(copperGrates)) {
            cir.setReturnValue(adjacentBlock.is(copperGrates) || super.skipRendering(blockState, adjacentBlock, direction));
        }
    }
}