package net.sydokiddo.chrysalis.mixin.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(HumanoidArmorLayer.class)
public class EntityRenderLayerMixin {

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void chrysalis$hideRenderedArmor(PoseStack poseStack, MultiBufferSource multiBufferSource, int color, LivingEntity livingEntity, float float1, float float2, float float3, float float4, float float5, float float6, CallbackInfo info) {
        Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
        if (livingEntity.hasEffect(invisibility) && Objects.requireNonNull(livingEntity.getEffect(invisibility)).getAmplifier() > 0) info.cancel();
    }

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    @Mixin(ElytraLayer.class)
    public static class ElytraLayerMixin {

        @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
        private void chrysalis$hideRenderedElytra(PoseStack poseStack, MultiBufferSource multiBufferSource, int color, LivingEntity livingEntity, float float1, float float2, float float3, float float4, float float5, float float6, CallbackInfo info) {
            Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
            if (livingEntity.hasEffect(invisibility) && Objects.requireNonNull(livingEntity.getEffect(invisibility)).getAmplifier() > 0) info.cancel();
        }
    }
}