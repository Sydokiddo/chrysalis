package net.sydokiddo.chrysalis.client.entities.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class CEntityOverlayRenderer<T extends Mob, M extends HierarchicalModel<T>> extends RenderLayer<T, M> {

    public CEntityOverlayRenderer(RenderLayerParent<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T entity, float f, float g, float h, float j, float k, float l) {
        RenderType renderType = this.renderType(entity);
        if (renderType != null) this.getParentModel().renderToBuffer(poseStack, multiBufferSource.getBuffer(renderType), i, LivingEntityRenderer.getOverlayCoords(entity, 0.0F));
    }

    public RenderType renderType(@NotNull LivingEntity livingEntity) {
        return null;
    }
}