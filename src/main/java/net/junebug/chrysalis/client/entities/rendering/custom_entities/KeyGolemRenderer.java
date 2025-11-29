package net.junebug.chrysalis.client.entities.rendering.custom_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.client.entities.models.KeyGolemModel;
import net.junebug.chrysalis.client.entities.rendering.render_states.CLivingEntityRenderState;
import net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem.KeyGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class KeyGolemRenderer extends MobRenderer<KeyGolem, CLivingEntityRenderState, KeyGolemModel> {

    /**
     * The entity rendering class for key golems.
     **/

    public static int firstPersonPackedLight = 15728881;

    public KeyGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new KeyGolemModel(context.bakeLayer(KeyGolemModel.LAYER)), 0.3F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CLivingEntityRenderState renderState) {
        return Chrysalis.resourceLocationId("textures/entity/key_golem/key_golem_golden.png");
    }

    @Override
    public @NotNull CLivingEntityRenderState createRenderState() {
        return new CLivingEntityRenderState();
    }

    @Override
    public void extractRenderState(@NotNull KeyGolem keyGolem, @NotNull CLivingEntityRenderState renderState, float tickCount) {
        CLivingEntityRenderState.livingEntity = keyGolem;
        super.extractRenderState(keyGolem, renderState, tickCount);
    }

    @Override
    public void render(@NotNull CLivingEntityRenderState renderState, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (this.isHeldInFirstPerson() && packedLight != firstPersonPackedLight) return;
        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    @Override
    protected void setupRotations(@NotNull CLivingEntityRenderState renderState, @NotNull PoseStack poseStack, float bodyRot, float scale) {

        if (this.isHeldInFirstPerson()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-20.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }

        else super.setupRotations(renderState, poseStack, bodyRot, scale);
    }

    private boolean isHeldInFirstPerson() {
        return Minecraft.getInstance().player != null && Minecraft.getInstance().options.getCameraType().isFirstPerson() && CLivingEntityRenderState.livingEntity instanceof KeyGolem keyGolem && keyGolem.isRidingSpecificPlayer(Minecraft.getInstance().player);
    }

    @Override
    protected float getShadowRadius(@NotNull CLivingEntityRenderState renderState) {
        if (CLivingEntityRenderState.livingEntity instanceof KeyGolem keyGolem && keyGolem.isRidingPlayer()) return 0.0F;
        return super.getShadowRadius(renderState);
    }

    @Override
    protected int getBlockLightLevel(@NotNull KeyGolem keyGolem, @NotNull BlockPos blockPos) {
        return 15;
    }
}