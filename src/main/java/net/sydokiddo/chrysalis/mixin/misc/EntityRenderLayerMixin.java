package net.sydokiddo.chrysalis.mixin.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.sydokiddo.chrysalis.misc.util.entities.ChrysalisEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(HumanoidArmorLayer.class)
public class EntityRenderLayerMixin {

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
    private void chrysalis$hideRenderedArmor(PoseStack poseStack, MultiBufferSource multiBufferSource, int color, HumanoidRenderState humanoidRenderState, float float1, float float2, CallbackInfo info) {
        Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
        if (ChrysalisEntityRenderState.livingEntity.hasEffect(invisibility) && Objects.requireNonNull(ChrysalisEntityRenderState.livingEntity.getEffect(invisibility)).getAmplifier() > 0) info.cancel();
    }

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    @Mixin(WingsLayer.class)
    public static class ElytraLayerMixin {

        @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
        private void chrysalis$hideRenderedElytra(PoseStack poseStack, MultiBufferSource multiBufferSource, int color, HumanoidRenderState humanoidRenderState, float float1, float float2, CallbackInfo info) {
            Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
            if (ChrysalisEntityRenderState.livingEntity.hasEffect(invisibility) && Objects.requireNonNull(ChrysalisEntityRenderState.livingEntity.getEffect(invisibility)).getAmplifier() > 0) info.cancel();
        }
    }
}