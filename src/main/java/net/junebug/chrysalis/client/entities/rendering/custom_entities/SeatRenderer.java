package net.junebug.chrysalis.client.entities.rendering.custom_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.junebug.chrysalis.client.entities.rendering.render_states.ChrysalisEntityRenderState;
import net.junebug.chrysalis.common.entities.custom_entities.Seat;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SeatRenderer extends EntityRenderer<Seat, ChrysalisEntityRenderState> {

    public SeatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ChrysalisEntityRenderState createRenderState() {
        return new ChrysalisEntityRenderState();
    }

    @Override
    public boolean shouldRender(@NotNull Seat seat, @NotNull Frustum frustum, double x, double y, double z) {
        return false;
    }

    @Override
    protected void renderNameTag(@NotNull ChrysalisEntityRenderState entityRenderState, @NotNull Component component, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int lightLevel) {}
}