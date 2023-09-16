package net.sydokiddo.chrysalis.client.entities.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class CInvisibilityCompatEntityOverlayRenderer<T extends Mob, M extends HierarchicalModel<T>> extends CEntityOverlayRenderer<T, M> {

    public CInvisibilityCompatEntityOverlayRenderer(RenderLayerParent<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull T entity, float f, float g, float h, float j, float k, float l) {
        if (!entity.isInvisible()) {
            super.render(poseStack, multiBufferSource, i, entity, f, g, h, j, k, l);
        }
    }
}