package net.sydokiddo.chrysalis.util.technical.camera;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

public class CameraSetup {

    /**
     * Gets various pieces of information about the client's camera.
     **/

    private final Camera camera;
    private float yaw;
    private float pitch;
    private float roll;

    public CameraSetup(Camera camera) {
        this.camera = camera;
    }

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