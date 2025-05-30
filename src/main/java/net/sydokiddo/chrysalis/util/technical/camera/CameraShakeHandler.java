package net.sydokiddo.chrysalis.util.technical.camera;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.Chrysalis;

@OnlyIn(Dist.CLIENT)
public class CameraShakeHandler {

    /**
     * Handles the camera shaking feature.
     **/

    private static int time = 0;

    private static float
        strength = 0.0F,
        frequency = 0.0F
    ;

    public static void shakeCamera(int time, float strength, float frequency) {
        if (strength > CameraShakeHandler.strength) {
            if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Emitting a camera shake of time: {}, strength: {}, and frequency: {}", time, strength, frequency);
            CameraShakeHandler.time = time;
            CameraShakeHandler.strength = strength;
            CameraShakeHandler.frequency = frequency;
        }
    }

    public static void resetCamera() {
        if (time == 0 && strength == 0.0F && frequency == 0.0F) return;
        if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Camera shake has been reset");
        time = 0;
        strength = 0.0F;
        frequency = 0.0F;
    }

    public static void handleCameraShake(CameraSetup cameraSetup) {

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (CameraShakeHandler.time > 0) {

            CameraShakeHandler.time--;

            float delta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
            float ticksExistedDelta = player.tickCount + delta;
            float strength = CameraShakeHandler.strength / 4.0F;
            float frequency = CameraShakeHandler.frequency / 2.0F;

            float yaw = (float) (cameraSetup.getYaw() + strength * Math.cos(ticksExistedDelta * frequency + 1.0F));
            float pitch = (float) (cameraSetup.getPitch() + strength * Math.cos(ticksExistedDelta * frequency + 2.0F));
            float roll = (float) (cameraSetup.getRoll() + strength * Math.cos(ticksExistedDelta * frequency));

            if (!Minecraft.getInstance().isPaused()) {
                cameraSetup.setYaw(yaw);
                cameraSetup.setPitch(pitch);
                cameraSetup.setRoll(roll);
            }

        } else {
            CameraShakeHandler.resetCamera();
        }
    }
}