package net.sydokiddo.chrysalis.client.entities.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.sydokiddo.chrysalis.client.entities.rendering.render_states.ChrysalisEntityRenderState;
import net.sydokiddo.chrysalis.registry.entities.custom_entities.Seat;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class SeatRenderer extends EntityRenderer<Seat, ChrysalisEntityRenderState> {

    public SeatRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ChrysalisEntityRenderState createRenderState() {
        return new ChrysalisEntityRenderState();
    }

    @Override
    public boolean shouldRender(Seat seat, Frustum frustum, double x, double y, double z) {
        return false;
    }

    @Override
    protected void renderNameTag(ChrysalisEntityRenderState entityRenderState, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int lightLevel) {}
}