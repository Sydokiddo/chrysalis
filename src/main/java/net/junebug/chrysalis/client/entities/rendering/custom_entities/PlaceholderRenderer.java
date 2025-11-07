package net.junebug.chrysalis.client.entities.rendering.custom_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.PlaceholderBlock;
import net.junebug.chrysalis.common.entities.custom_entities.PlaceholderBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class PlaceholderRenderer implements BlockEntityRenderer<PlaceholderBlockEntity> {

    private final EntityRenderDispatcher entityRenderDispatcher;

    public PlaceholderRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderDispatcher = context.getEntityRenderer();
    }

    @Override
    public void render(@NotNull PlaceholderBlockEntity placeholderBlockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {

        if (placeholderBlockEntity.getBlockState().getValue(CBlockStateProperties.PLACEHOLDER_MODEL_STATE) == PlaceholderBlock.PlaceholderModelState.BILLBOARD) {

            poseStack.pushPose();
            poseStack.translate(0.5F, 0.25F, 0.5F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(Chrysalis.resourceLocationId("textures/entity/block_entities/placeholder.png")));
            PoseStack.Pose lastPoseStack = poseStack.last();

            this.vertex(vertexConsumer, lastPoseStack, packedLight, 0.0F, 0, 0, 1);
            this.vertex(vertexConsumer, lastPoseStack, packedLight, 1.0F, 0, 1, 1);
            this.vertex(vertexConsumer, lastPoseStack, packedLight, 1.0F, 1, 1, 0);
            this.vertex(vertexConsumer, lastPoseStack, packedLight, 0.0F, 1, 0, 0);

            poseStack.popPose();
        }
    }

    private void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, int packedLight, float x, int y, int u, int v) {
        vertexConsumer.addVertex(pose, x - 0.5F, y - 0.25F, 0.0F).setColor(-1).setLight(packedLight).setOverlay(OverlayTexture.NO_OVERLAY).setUv(u, v).setNormal(pose, 0.0F, 0.5F, 0.0F);
    }
}