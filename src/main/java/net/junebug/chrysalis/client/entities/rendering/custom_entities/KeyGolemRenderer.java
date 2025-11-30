package net.junebug.chrysalis.client.entities.rendering.custom_entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.client.entities.models.KeyGolemModel;
import net.junebug.chrysalis.client.entities.rendering.EntityOverlayRenderer;
import net.junebug.chrysalis.client.entities.rendering.render_states.CLivingEntityRenderState;
import net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem.KeyGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class KeyGolemRenderer extends MobRenderer<KeyGolem, CLivingEntityRenderState, KeyGolemModel> {

    public KeyGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new KeyGolemModel(context.bakeLayer(KeyGolemModel.LAYER)), 0.3F);
        this.addLayer(new KeyGolemEyesLayer(this));
        this.addLayer(new KeyGolemTranslucentLayer(this));
    }

    // region Base Rendering

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CLivingEntityRenderState renderState) {
        return Chrysalis.resourceLocationId("textures/entity/key_golem/key_golem_empty.png");
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
        if (CLivingEntityRenderState.livingEntity instanceof KeyGolem keyGolem && this.isHeldInFirstPerson() && packedLight != getFirstPersonLightLevel(keyGolem)) return;
        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    // endregion

    // region Miscellaneous Rendering

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

        float original = super.getShadowRadius(renderState);

        if (CLivingEntityRenderState.livingEntity instanceof KeyGolem keyGolem) {
            if (keyGolem.isRidingPlayer()) return 0.0F;
            else if (keyGolem.isDespawnTriggered()) return Mth.clampedLerp(original, 0.0F, 1.0F - (float) keyGolem.getTicksUntilDespawn() / 25.0F);
        }

        return original;
    }

    @Override
    protected int getBlockLightLevel(@NotNull KeyGolem keyGolem, @NotNull BlockPos blockPos) {
        return Math.max(super.getBlockLightLevel(keyGolem, blockPos), keyGolem.getBrightness());
    }

    public static int getFirstPersonLightLevel(KeyGolem keyGolem) {
        BlockPos blockPos = BlockPos.containing(keyGolem.getLightProbePosition(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)));
        return LightTexture.pack(Math.max(keyGolem.level().getBrightness(LightLayer.BLOCK, blockPos), keyGolem.getBrightness()), keyGolem.level().getBrightness(LightLayer.SKY, blockPos)) + 1;
    }

    // endregion

    @OnlyIn(Dist.CLIENT)
    private static class KeyGolemEyesLayer extends EntityOverlayRenderer<CLivingEntityRenderState, KeyGolemModel> {

        private KeyGolemEyesLayer(RenderLayerParent<CLivingEntityRenderState, KeyGolemModel> renderLayerParent) {
            super(renderLayerParent, true);
        }

        @Override
        public RenderType renderType() {
            return RenderType.entityCutoutNoCull(Chrysalis.resourceLocationId("textures/entity/key_golem/key_golem_eyes.png"));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static class KeyGolemTranslucentLayer extends EntityOverlayRenderer<CLivingEntityRenderState, KeyGolemModel> {

        @SuppressWarnings("unused")
        private static final RenderType
            GOLDEN_TEXTURE = RenderType.entityTranslucent(Chrysalis.resourceLocationId("textures/entity/key_golem/key_golem_golden.png")),
            DIAMOND_TEXTURE = RenderType.entityTranslucent(Chrysalis.resourceLocationId("textures/entity/key_golem/key_golem_diamond.png"))
        ;

        private KeyGolemTranslucentLayer(RenderLayerParent<CLivingEntityRenderState, KeyGolemModel> renderLayerParent) {
            super(renderLayerParent, true);
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int color, @NotNull CLivingEntityRenderState renderState, float float1, float float2) {
            RenderType renderType = this.renderType();
            if (renderType == null || !(CLivingEntityRenderState.livingEntity instanceof KeyGolem keyGolem) || this.hideWhenInvisible && keyGolem.isInvisible()) return;
            this.getParentModel().renderToBuffer(poseStack, multiBufferSource.getBuffer(renderType), color, LivingEntityRenderer.getOverlayCoords(renderState, 0.0F), ARGB.colorFromFloat(keyGolem.isFake() ? 0.75F : 1.0F, 1.0F, 1.0F, 1.0F));
        }

        @Override
        public RenderType renderType() {
            if (CLivingEntityRenderState.livingEntity instanceof KeyGolem keyGolem) return RenderType.entityTranslucent(Chrysalis.resourceLocationId("textures/entity/key_golem/key_golem_" + keyGolem.getVariant().getSerializedName() + ".png"));
            return null;
        }
    }
}