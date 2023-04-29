package net.sydokiddo.chrysalis.misc.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;

@SuppressWarnings("all")
@Environment(EnvType.CLIENT)
public class CameraSetup {

    private final Camera camera;
    private float roll;

    public CameraSetup(Camera camera) {
        this.camera = camera;
    }

    public Camera getInfo() {
        return camera;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getRoll() {
        return roll;
    }
}