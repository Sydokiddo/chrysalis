package net.sydokiddo.chrysalis.client.entities.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public abstract class CInvisibilityCompatEntityOverlayRenderer extends CEntityOverlayRenderer<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> {

    public CInvisibilityCompatEntityOverlayRenderer(RenderLayerParent<LivingEntityRenderState, EntityModel<LivingEntityRenderState>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int color, LivingEntityRenderState livingEntityRenderState, float float1, float float2) {
        if (!livingEntityRenderState.isInvisible) super.render(poseStack, multiBufferSource, color, livingEntityRenderState, float1, float2);
    }
}