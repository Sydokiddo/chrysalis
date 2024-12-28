package net.sydokiddo.chrysalis.misc.util.camera;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
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

            float delta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
            float ticksExistedDelta = player.tickCount + delta;
            float strength = CameraShakeHandler.strength / 4.0F;
            float frequency = CameraShakeHandler.frequency / 2.0F;

            float yaw = (float) (cameraSetup.getYaw() + strength * Math.cos(ticksExistedDelta * frequency + 1));
            float pitch = (float) (cameraSetup.getPitch() + strength * Math.cos(ticksExistedDelta * frequency + 2));
            float roll = (float) (cameraSetup.getRoll() + strength * Math.cos(ticksExistedDelta * frequency));

            if (!Minecraft.getInstance().isPaused()) {
                if (CameraShakeHandler.time > 5) {
                    cameraSetup.setYaw(yaw);
                    cameraSetup.setPitch(pitch);
                    cameraSetup.setRoll(roll);
                } else {
                    cameraSetup.setYaw(Mth.lerp(delta, yaw, 0));
                    cameraSetup.setPitch(Mth.lerp(delta, pitch, 0));
                    cameraSetup.setRoll(Mth.lerp(delta, roll, 0));
                }
            }

        } else {
            CameraShakeHandler.resetCamera();
        }
    }
}