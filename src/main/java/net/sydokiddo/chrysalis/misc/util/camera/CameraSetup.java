package net.sydokiddo.chrysalis.misc.util.camera;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class CameraSetup {

    private final Camera camera;
    private float yaw;
    private float pitch;
    private float roll;

    public CameraSetup(Camera camera) {
        this.camera = camera;
    }

    /**
     * Gets various information about the client's camera.
     **/

    public static CameraSetup getCameraSetup() {
        return new CameraSetup(Minecraft.getInstance().gameRenderer.getMainCamera());
    }

    public Camera getInfo() {
        return this.camera;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return this.roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}