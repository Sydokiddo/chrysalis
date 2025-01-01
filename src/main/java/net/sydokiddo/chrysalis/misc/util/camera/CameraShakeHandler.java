package net.sydokiddo.chrysalis.misc.util.camera;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.sydokiddo.chrysalis.Chrysalis;

@Environment(EnvType.CLIENT)
public class CameraShakeHandler {

    private static int time = 0;
    private static float strength = 0;
    private static float frequency = 0;

    public static void shakeCamera(int time, float strength, float frequency) {
        if (strength > CameraShakeHandler.strength) {
            if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Emitting a camera shake of time: {}, strength: {}, and frequency: {}", time, strength, frequency);
            CameraShakeHandler.time = time;
            CameraShakeHandler.strength = strength;
            CameraShakeHandler.frequency = frequency;
        }
    }

    public static void resetCamera() {
        if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Camera shake has been reset");
        time = 0;
        strength = 0;
        frequency = 0;
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

            float yaw = (float) (cameraSetup.getYaw() + strength * Math.cos(ticksExistedDelta * frequency + 1));
            float pitch = (float) (cameraSetup.getPitch() + strength * Math.cos(ticksExistedDelta * frequency + 2));
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