package net.sydokiddo.chrysalis.mixin.misc;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.sydokiddo.chrysalis.misc.util.camera.CameraSetup;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @ModifyExpressionValue(method = "renderLevel", at = @At(value = "NEW", target = "()Lcom/mojang/blaze3d/vertex/PoseStack;"))
    private PoseStack chrysalis$renderCameraShake(PoseStack poseStack) {
        CameraSetup cameraSetup = new CameraSetup(Minecraft.getInstance().gameRenderer.getMainCamera());
        CameraShakeHandler.handleCameraShake(cameraSetup);
        poseStack.mulPose(Axis.XP.rotationDegrees(cameraSetup.getYaw()));
        poseStack.mulPose(Axis.YP.rotationDegrees(cameraSetup.getPitch()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(cameraSetup.getRoll()));
        return poseStack;
    }
}