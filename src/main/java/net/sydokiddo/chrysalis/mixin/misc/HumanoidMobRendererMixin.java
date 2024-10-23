package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.sydokiddo.chrysalis.misc.util.entities.ChrysalisEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HumanoidMobRenderer.class)
public abstract class HumanoidMobRendererMixin {

    @Inject(method = "extractHumanoidRenderState", at = @At("HEAD"))
    private static void chrysalis$addHumanoidRenderStates(LivingEntity livingEntity, HumanoidRenderState humanoidRenderState, float tickCount, CallbackInfo info) {
        ChrysalisEntityRenderState.livingEntity = livingEntity;
    }
}