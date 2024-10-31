package net.sydokiddo.chrysalis.mixin.misc;

import com.mojang.blaze3d.shaders.FogShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.sydokiddo.chrysalis.misc.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "setupFog", at = @At(value = "RETURN"), cancellable = true)
    private static void chrysalis$applyCustomFog(Camera camera, FogRenderer.FogMode fogMode, Vector4f vector4f, float viewDistance, boolean thickFog, float tickDelta, CallbackInfoReturnable<FogParameters> cir) {
        if (camera.getEntity() instanceof LivingEntity livingEntity && hasRadianceEffect(livingEntity)) cir.setReturnValue(EventHelper.createCustomFog(0.25F, 1.0F, FogShape.SPHERE, vector4f));
    }

    @Inject(method = "computeFogColor", at = @At(value = "RETURN"), cancellable = true)
    private static void chrysalis$applyCustomFogColor(Camera camera, float tickDelta, ClientLevel clientLevel, int int1, float float1, CallbackInfoReturnable<Vector4f> cir) {
        if (camera.getEntity() instanceof LivingEntity livingEntity && hasRadianceEffect(livingEntity)) {
            MobEffectInstance radianceEffect = Objects.requireNonNull(livingEntity.getEffect(getRadianceEffect()));
            float intensity = radianceEffect.isInfiniteDuration() ? 1.0F : Mth.clamp(radianceEffect.getDuration() / 20.0F, 0.0F, 1.0F);
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