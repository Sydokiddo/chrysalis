package net.junebug.chrysalis.mixin.blocks;

import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.common.CConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HalfTransparentBlock.class)
public class HalfTransparentBlockMixin extends Block {

    private HalfTransparentBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * Occludes blocked faces of any glass or grate block when adjacent to each other.
     **/

    @Inject(at = @At("HEAD"), method = "skipRendering", cancellable = true)
    private void chrysalis$occludeBlockFaces(BlockState blockState, BlockState adjacentBlockState, Direction direction, CallbackInfoReturnable<Boolean> cir) {

        boolean original = super.skipRendering(blockState, adjacentBlockState, direction);

        TagKey<Block> glassBlocks = Tags.Blocks.GLASS_BLOCKS;
        if (CConfig.IMPROVED_GLASS_RENDERING.get() && blockState.is(glassBlocks)) cir.setReturnValue(adjacentBlockState.is(glassBlocks) || original);

        TagKey<Block> grates = CTags.GRATES;
        if (CConfig.IMPROVED_GRATE_RENDERING.get() && blockState.is(grates)) cir.setReturnValue(adjacentBlockState.is(grates) || original);
    }

    @SuppressWarnings("unused")
    @Mixin(IronBarsBlock.class)
    private static abstract class GlassPaneBlockMixin extends CrossCollisionBlock {

        private GlassPaneBlockMixin(float nodeWidth, float extensionWidth, float nodeHeight, float extensionHeight, float collisionHeight, Properties properties) {
            super(nodeWidth, extensionWidth, nodeHeight, extensionHeight, collisionHeight, properties);
        }

        /**
         * Occludes blocked faces of any glass pane block when adjacent to each other.
         **/

        @Inject(at = @At("HEAD"), method = "skipRendering", cancellable = true)
        private void chrysalis$occludeGlassPaneFaces(BlockState blockState, BlockState adjacentBlock, Direction direction, CallbackInfoReturnable<Boolean> cir) {

            TagKey<Block> glassPanes = Tags.Blocks.GLASS_PANES;

            if (CConfig.IMPROVED_GLASS_RENDERING.get() && blockState.is(glassPanes) && adjacentBlock.is(glassPanes)) {
                if (!direction.getAxis().isHorizontal()) cir.setReturnValue(true);
                if (blockState.getValue(PROPERTY_BY_DIRECTION.get(direction)) && adjacentBlock.getValue(PROPERTY_BY_DIRECTION.get(direction.getOpposite()))) cir.setReturnValue(true);
            }
        }
    }
}