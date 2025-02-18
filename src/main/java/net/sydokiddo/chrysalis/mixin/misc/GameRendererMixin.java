package net.sydokiddo.chrysalis.mixin.misc;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.sydokiddo.chrysalis.util.technical.camera.CameraSetup;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakeHandler;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow protected abstract void setPostEffect(ResourceLocation resourceLocation);
    @Shadow @Final private Minecraft minecraft;

    /**
     * Adds spectator mode shaders to entities within specific tags or entities that are under specific effects.
     **/

    @Inject(method = "checkEntityPostEffect", at = @At("RETURN"))
    private void chrysalis$addEntitySpectatorShaders(Entity entity, CallbackInfo info) {

        if (!this.minecraft.options.getCameraType().isFirstPerson() || !(entity instanceof LivingEntity livingEntity)) return;
        EntityType<?> entityType = livingEntity.getType();

        if (entityType.is(ChrysalisTags.HAS_ARTHROPOD_SIGHT) || livingEntity.hasEffect(ChrysalisEffects.ARTHROPOD_SIGHT)) this.setPostEffect(ResourceLocation.withDefaultNamespace("spider"));
        if (entityType.is(ChrysalisTags.HAS_CREEPER_SIGHT) || livingEntity.hasEffect(ChrysalisEffects.CREEPER_SIGHT)) this.setPostEffect(ResourceLocation.withDefaultNamespace("creeper"));
        if (entityType.is(ChrysalisTags.HAS_ENDER_SIGHT) || livingEntity.hasEffect(ChrysalisEffects.ENDER_SIGHT)) this.setPostEffect(ResourceLocation.withDefaultNamespace("invert"));
    }

    /**
     * Rotates the client's camera depending on the triggered camera shake values.
     **/

    @ModifyExpressionValue(method = "renderLevel", at = @At(value = "NEW", target = "()Lcom/mojang/blaze3d/vertex/PoseStack;"))
    private PoseStack chrysalis$renderCameraShake(PoseStack poseStack) {
        CameraSetup cameraSetup = CameraSetup.getCameraSetup();
        CameraShakeHandler.handleCameraShake(cameraSetup);
        poseStack.mulPose(Axis.XP.rotationDegrees(cameraSetup.getYaw()));
        poseStack.mulPose(Axis.YP.rotationDegrees(cameraSetup.getPitch()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(cameraSetup.getRoll()));
        return poseStack;
    }

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    @Mixin(FogRenderer.class)
    public static class FogRendererMixin {

        /**
         * Applies a custom fog effect to the player's screen while under the radiance effect.
         **/

        @Shadow private static long biomeChangedTime;

        @Inject(method = "setupFog", at = @At(value = "RETURN"), cancellable = true)
        private static void chrysalis$applyCustomFog(Camera camera, FogRenderer.FogMode fogMode, Vector4f vector4f, float viewDistance, boolean thickFog, float tickDelta, CallbackInfoReturnable<FogParameters> cir) {
            if (camera.getEntity() instanceof LivingEntity livingEntity && hasRadianceEffect(livingEntity)) cir.setReturnValue(EventHelper.createCustomFog(0.25F, 1.0F, FogShape.SPHERE, vector4f));
        }

        @Inject(method = "computeFogColor", at = @At(value = "RETURN"), cancellable = true)
        private static void chrysalis$applyCustomFogColor(Camera camera, float tickDelta, ClientLevel clientLevel, int int1, float float1, CallbackInfoReturnable<Vector4f> cir) {
            if (camera.getEntity() instanceof LivingEntity livingEntity && hasRadianceEffect(livingEntity)) {

                MobEffectInstance radianceEffect = Objects.requireNonNull(livingEntity.getEffect(getRadianceEffect()));
                float intensity = Mth.lerp(radianceEffect.getBlendFactor(livingEntity, tickDelta), 0.0F, radianceEffect.isInfiniteDuration() ? 1.0F : Mth.clamp(radianceEffect.getDuration() / 20.0F, 0.0F, 1.0F));

                biomeChangedTime = -1L;
                cir.setReturnValue(new Vector4f(intensity, intensity, intensity, intensity));
            }
        }

        @Unique
        private static Holder<MobEffect> getRadianceEffect() {
            return ChrysalisEffects.RADIANCE;
        }

        @Unique
        private static boolean hasRadianceEffect(LivingEntity livingEntity) {
            return livingEntity.hasEffect(getRadianceEffect());
        }
    }
}