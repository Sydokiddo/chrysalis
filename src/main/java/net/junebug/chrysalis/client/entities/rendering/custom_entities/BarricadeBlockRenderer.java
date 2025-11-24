package net.junebug.chrysalis.client.entities.rendering.custom_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.junebug.chrysalis.common.entities.custom_entities.block_entities.BarricadeBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BarricadeBlockRenderer implements BlockEntityRenderer<BarricadeBlockEntity> {

    /**
     * The block entity rendering class for barricade blocks.
     **/

    private final BlockRenderDispatcher blockRenderer;

    public BarricadeBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(@NotNull BarricadeBlockEntity barricadeBlockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {

        if (barricadeBlockEntity.getLevel() != null && !barricadeBlockEntity.getDisguisedBlockState().isEmpty()) {

            boolean isVisible = barricadeBlockEntity.getDisguisedBlockState().getRenderShape() != RenderShape.INVISIBLE;
            boolean canRender = this.canRenderWithInvisibleRenderShape(barricadeBlockEntity.getDisguisedBlockState()) || isVisible;
            if (!canRender) return;

            poseStack.pushPose();
            this.blockRenderer.renderSingleBlock(isVisible ? barricadeBlockEntity.getDisguisedBlockState() : Blocks.STONE.defaultBlockState(), poseStack, multiBufferSource, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, this.getBlockRenderType(barricadeBlockEntity.getDisguisedBlockState()));
            poseStack.popPose();
        }
    }

    private boolean canRenderWithInvisibleRenderShape(BlockState blockState) {
        return blockState.getBlock() instanceof EndPortalBlock || blockState.getBlock() instanceof EndGatewayBlock;
    }

    private RenderType getBlockRenderType(BlockState blockState) {
        if (blockState.getBlock() instanceof EndPortalBlock) return RenderType.endPortal();
        else if (blockState.getBlock() instanceof EndGatewayBlock) return RenderType.endGateway();
        else return null;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}