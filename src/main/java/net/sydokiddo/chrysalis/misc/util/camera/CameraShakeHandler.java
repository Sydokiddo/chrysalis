package net.sydokiddo.chrysalis.misc.util.camera;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class CameraShakeHandler {

    private static int time = 0;
    private static float strength = 0;
    private static float frequency = 0;

    public static void shakeCamera(int time, float strength, float frequency) {
        if (strength > CameraShakeHandler.strength) {
            CameraShakeHandler.time = time;
            CameraShakeHandler.strength = strength;
            CameraShakeHandler.frequency = frequency;
        }
    }

    public static void resetCamera() {
        time = 0;
        strength = 0;
        frequency = 0;
    }

    public static void handleCameraShake(CameraSetup cameraSetup) {

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (CameraShakeHandler.time > 0) {

            CameraShakeHandler.time--;

            float delta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
            float ticksExistedDelta = player.tickCount + delta;

            float strength = CameraShakeHandler.strength / 4.0F;
            float frequency = CameraShakeHandler.frequency / 2.0F;

            if (!Minecraft.getInstance().isPaused()) {
                cameraSetup.setYaw((float) (cameraSetup.getYaw() + strength * Math.cos(ticksExistedDelta * frequency + 1)));
                cameraSetup.setPitch((float) (cameraSetup.getPitch() + strength * Math.cos(ticksExistedDelta * frequency + 2)));
                cameraSetup.setRoll((float) (cameraSetup.getRoll() + strength * Math.cos(ticksExistedDelta * frequency)));
            }

        } else if (CameraShakeHandler.strength != 0) {
            CameraShakeHandler.resetCamera();
        }
    }
}