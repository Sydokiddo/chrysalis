package net.junebug.chrysalis.mixin.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.client.entities.rendering.render_states.CEntityRenderState;
import net.junebug.chrysalis.client.entities.rendering.render_states.CLivingEntityRenderState;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.common.status_effects.CStatusEffects;
import net.junebug.chrysalis.common.CConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    /**
     * Allows for the entity field to be accessed from the entity renderer class.
     **/

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void chrysalis$addEntityRenderStates(Entity entity, EntityRenderState renderState, float tickCount, CallbackInfo info) {
        CEntityRenderState.entity = entity;
    }

    /**
     * Changes the illumination value of the player's first-person hand depending on the radiance effect.
     **/

    @Inject(method = "getBlockLightLevel", at = @At("RETURN"), cancellable = true)
    private void chrysalis$playerHandRadianceGlowing(Entity entity, BlockPos blockPos, CallbackInfoReturnable<Integer> cir) {

        Minecraft minecraft = Minecraft.getInstance();
        Holder<MobEffect> radianceEffect = Holder.direct(CStatusEffects.RADIANCE.get());

        if (entity instanceof Player player && player == minecraft.player && player.hasEffect(radianceEffect) && minecraft.options.getCameraType().isFirstPerson()) {
            MobEffectInstance radianceEffectInstance = Objects.requireNonNull(player.getEffect(radianceEffect));
            cir.setReturnValue(Mth.lerpInt(radianceEffectInstance.getBlendFactor(player, minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false)), cir.getReturnValue(), 15));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(LivingEntityRenderer.class)
    public static class LivingEntityRendererMixin {

        /**
         * Allows for the livingEntity field to be accessed from the living entity renderer class.
         **/

        @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
        private void chrysalis$addLivingEntityRenderStates(LivingEntity livingEntity, LivingEntityRenderState renderState, float tickCount, CallbackInfo info) {
            CLivingEntityRenderState.livingEntity = livingEntity;
        }

        /**
         * Determines whether living entities in specific tags flip over upon death similarly to spiders, or if they don't flip over at all.
         **/

        @Inject(method = "getFlipDegrees", at = @At("HEAD"), cancellable = true)
        private void chrysalis$changeEntityFlippingUponDeath(CallbackInfoReturnable<Float> cir) {
            if (CLivingEntityRenderState.livingEntity.getType().is(CTags.DOES_NOT_FLIP_OVER_UPON_DEATH)) cir.setReturnValue(0.0F);
            else if (CLivingEntityRenderState.livingEntity.getType().is(CTags.FLIPS_OVER_UPON_DEATH)) cir.setReturnValue(180.0F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(PlayerRenderer.class)
    public static class PlayerRendererMixin {

        /**
         * Hides a player's name tag if they are wearing any item in the hides_name_tags item tag.
         **/

        @Inject(method = "renderNameTag(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
        private void chrysalis$hidePlayerNameTag(PlayerRenderState playerRenderState, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, CallbackInfo info) {

            if (!CConfig.NAME_TAG_HIDING.get()) return;

            for (ItemStack equipmentSlot : CLivingEntityRenderState.livingEntity.getArmorSlots()) {
                if (equipmentSlot.is(CTags.HIDES_NAME_TAGS)) {
                    if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("{} is wearing a name tag hiding item!", CLivingEntityRenderState.livingEntity.getName().getString());
                    info.cancel();
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(HumanoidArmorLayer.class)
    public static class HumanoidArmorLayerMixin {

        /**
         * Hides rendered armor while the entity is under the invisibility 2 effect or higher.
         **/

        @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
        private void chrysalis$hideRenderedArmor(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, HumanoidRenderState humanoidRenderState, float yRot, float xRot, CallbackInfo info) {
            Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
            if (CLivingEntityRenderState.livingEntity.hasEffect(invisibility) && Objects.requireNonNull(CLivingEntityRenderState.livingEntity.getEffect(invisibility)).getAmplifier() > 0) info.cancel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(WingsLayer.class)
    public static class WingsLayerMixin {

        /**
         * Hides rendered elytra while the entity is under the invisibility 2 effect or higher.
         **/

        @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"), cancellable = true)
        private void chrysalis$hideRenderedElytra(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, HumanoidRenderState humanoidRenderState, float yRot, float xRot, CallbackInfo info) {
            Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
            if (CLivingEntityRenderState.livingEntity.hasEffect(invisibility) && Objects.requireNonNull(CLivingEntityRenderState.livingEntity.getEffect(invisibility)).getAmplifier() > 0) info.cancel();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(ElytraModel.class)
    public static class ElytraModelMixin {

        /**
         * Fixes stretched uvs on the elytra model.
         **/

        @ModifyArg(method = "createLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/builders/CubeListBuilder;addBox(FFFFFFLnet/minecraft/client/model/geom/builders/CubeDeformation;)Lnet/minecraft/client/model/geom/builders/CubeListBuilder;"), index = 6)
        private static CubeDeformation chrysalis$fixElytraUVs(CubeDeformation cubeDeformation) {
            if (CConfig.FIXED_ELYTRA_MODEL.get()) return new CubeDeformation(1.0F, 1.0F, 0.2F);
            return new CubeDeformation(1.0F);
        }
    }
}