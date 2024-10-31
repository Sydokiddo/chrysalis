package net.sydokiddo.chrysalis.mixin.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.sydokiddo.chrysalis.client.entities.rendering.render_states.ChrysalisEntityRenderState;
import net.sydokiddo.chrysalis.client.entities.rendering.render_states.ChrysalisLivingEntityRenderState;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void chrysalis$addEntityRenderStates(Entity entity, EntityRenderState entityRenderState, float tickCount, CallbackInfo info) {
        ChrysalisEntityRenderState.entity = entity;
    }

    @Inject(method = "getBlockLightLevel", at = @At("HEAD"), cancellable = true)
    private void chrysalis$playerHandRadianceGlowing(Entity entity, BlockPos blockPos, CallbackInfoReturnable<Integer> cir) {
        Holder<MobEffect> radianceEffect = ChrysalisEffects.RADIANCE;
        if (entity instanceof Player player && player == Minecraft.getInstance().player && player.hasEffect(radianceEffect) && Minecraft.getInstance().options.getCameraType().isFirstPerson()) cir.setReturnValue(Math.min(Objects.requireNonNull(player.getEffect(radianceEffect)).getDuration(), 15));
    }

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    @Mixin(LivingEntityRenderer.class)
    public static abstract class LivingEntityRendererMixin {

        @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
        private void chrysalis$addLivingEntityRenderStates(LivingEntity livingEntity, LivingEntityRenderState livingEntityRenderState, float tickCount, CallbackInfo info) {
            ChrysalisLivingEntityRenderState.livingEntity = livingEntity;
        }
    }

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    @Mixin(HumanoidArmorLayer.class)
    public static class HumanoidArmorLayerMixin {

        @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
        private void chrysalis$hideRenderedArmor(PoseStack poseStack, MultiBufferSource multiBufferSource, int color, HumanoidRenderState humanoidRenderState, float float1, float float2, CallbackInfo info) {
            Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
            if (ChrysalisLivingEntityRenderState.livingEntity.hasEffect(invisibility) && Objects.requireNonNull(ChrysalisLivingEntityRenderState.livingEntity.getEffect(invisibility)).getAmplifier() > 0) info.cancel();
        }
    }

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    @Mixin(WingsLayer.class)
    public static class WingsLayerMixin {

        @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
        private void chrysalis$hideRenderedElytra(PoseStack poseStack, MultiBufferSource multiBufferSource, int color, HumanoidRenderState humanoidRenderState, float float1, float float2, CallbackInfo info) {
            Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
            if (ChrysalisLivingEntityRenderState.livingEntity.hasEffect(invisibility) && Objects.requireNonNull(ChrysalisLivingEntityRenderState.livingEntity.getEffect(invisibility)).getAmplifier() > 0) info.cancel();
        }
    }
}