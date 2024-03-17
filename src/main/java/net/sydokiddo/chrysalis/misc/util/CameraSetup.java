package net.sydokiddo.chrysalis.misc.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class CameraSetup {

    private final Camera camera;
    private float roll;

    public CameraSetup(Camera camera) {
        this.camera = camera;
    }

    /**
     * Gets various information about the client's camera.
     **/

    public Camera getInfo() {
        return this.camera;
    }

    public float getRoll() {
        return this.roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }
}