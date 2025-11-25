package net.junebug.chrysalis.mixin.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.junebug.chrysalis.common.blocks.custom_blocks.BarricadeFullBlock;
import net.junebug.chrysalis.common.misc.CTags;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {

    /**
     * If a barricade block has a disguised block state set, the block state for occlusion will be said to said disguised block state rather than the barricade block's block state.
     **/

    @Redirect(method = "tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;shouldRenderFace(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"))
    private boolean chrysalis$occludeBarricadeBlockFaces(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, BlockState adjacentState, Direction direction) {

        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
        mutableBlockPos.setWithOffset(blockPos, direction);

        if (blockGetter.getBlockState(blockPos).getBlock() instanceof BarricadeFullBlock barricade && barricade.getBarricadeBlockEntity(blockGetter, blockPos).isPresent() && !barricade.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState().isEmpty()) blockState = barricade.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState();
        if (blockGetter.getBlockState(mutableBlockPos).getBlock() instanceof BarricadeFullBlock barricade && barricade.getBarricadeBlockEntity(blockGetter, mutableBlockPos).isPresent() && !barricade.getBarricadeBlockEntity(blockGetter, mutableBlockPos).get().getDisguisedBlockState().isEmpty()) adjacentState = barricade.getBarricadeBlockEntity(blockGetter, mutableBlockPos).get().getDisguisedBlockState();

        return Block.shouldRenderFace(blockGetter, blockPos, blockState, adjacentState, direction);
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(BlockRenderDispatcher.class)
    public static abstract class BlockRenderDispatcherMixin {

        @Shadow public abstract BlockModelShaper getBlockModelShaper();
        @Shadow public abstract ModelBlockRenderer getModelRenderer();
        @Shadow @Final private RandomSource random;

        /**
         * Blocks in the can_always_render_breaking_overlay tag can always render the breaking overlay texture even when they're invisible.
         **/

        @Inject(at = @At("HEAD"), method = "renderBreakingTexture(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/neoforged/neoforge/client/model/data/ModelData;)V")
        private void chrysalis$alwaysRenderBreakingOverlay(BlockState blockState, BlockPos blockPos, BlockAndTintGetter blockAndTintGetter, PoseStack poseStack, VertexConsumer vertexConsumer, ModelData modelData, CallbackInfo info) {
            if (blockState.getRenderShape() == RenderShape.INVISIBLE && blockState.is(CTags.CAN_ALWAYS_RENDER_BREAKING_OVERLAY)) {
                BakedModel bakedModel = this.getBlockModelShaper().getBlockModel(blockState);
                this.getModelRenderer().tesselateBlock(blockAndTintGetter, bakedModel, blockState, blockPos, poseStack, vertexConsumer, true, this.random, blockState.getSeed(blockPos), OverlayTexture.NO_OVERLAY, bakedModel.getModelData(blockAndTintGetter, blockPos, blockState, modelData), RenderType.translucentMovingBlock());
            }
        }
    }
}