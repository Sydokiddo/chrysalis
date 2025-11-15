package net.junebug.chrysalis.client.entities.rendering.custom_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.PlaceholderBlock;
import net.junebug.chrysalis.common.entities.custom_entities.PlaceholderBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class PlaceholderBlockRenderer implements BlockEntityRenderer<PlaceholderBlockEntity> {

    /**
     * The block entity rendering class for placeholder blocks.
     **/

    private final EntityRenderDispatcher entityRenderer;
    private final BlockRenderDispatcher blockRenderer;

    public PlaceholderBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(@NotNull PlaceholderBlockEntity placeholderBlockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {

        Player player = Minecraft.getInstance().player;

        if (player != null && player.getAbilities().invulnerable && !placeholderBlockEntity.getBlockStateToUpdate().isEmpty()) {

            poseStack.pushPose();

            poseStack.translate(0.5F, (Mth.sin(placeholderBlockEntity.animationTime / 10.0F) * 0.1F) + 1.5F, 0.5F);
            poseStack.scale(0.45F, 0.45F, 0.45F);
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(placeholderBlockEntity.animationTime)));
            poseStack.translate(-0.5F, -0.5F, -0.5F);

            this.blockRenderer.renderSingleBlock(placeholderBlockEntity.getBlockStateToUpdate(), poseStack, multiBufferSource, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
            poseStack.popPose();
        }

        if (placeholderBlockEntity.getBlockState().getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlock.PlaceholderBlockModelState.BILLBOARD) {

            poseStack.pushPose();
            poseStack.translate(0.5F, 0.25F, 0.5F);
            poseStack.mulPose(this.entityRenderer.cameraOrientation());

            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(Chrysalis.resourceLocationId("textures/entity/block_entities/placeholder_block.png")));
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