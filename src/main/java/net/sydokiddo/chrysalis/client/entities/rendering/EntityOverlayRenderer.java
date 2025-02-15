package net.sydokiddo.chrysalis.client.entities.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public abstract class EntityOverlayRenderer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {

    /**
     * A template renderer class for entity texture overlays.
     **/

    private final boolean hideWhenInvisible;

    public EntityOverlayRenderer(RenderLayerParent<S, M> renderLayerParent, boolean hideWhenInvisible) {
        super(renderLayerParent);
        this.hideWhenInvisible = hideWhenInvisible;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int color, S entityRenderState, float float1, float float2) {
        if (this.hideWhenInvisible && entityRenderState.isInvisible) return;
        RenderType renderType = this.renderType();
        if (renderType != null) this.getParentModel().renderToBuffer(poseStack, multiBufferSource.getBuffer(renderType), color, OverlayTexture.NO_OVERLAY);
    }

    public abstract RenderType renderType();
}