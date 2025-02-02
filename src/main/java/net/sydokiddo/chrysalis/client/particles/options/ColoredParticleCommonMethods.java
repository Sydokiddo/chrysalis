package net.sydokiddo.chrysalis.client.particles.options;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public interface ColoredParticleCommonMethods {

    default int getColor() {
        return 0;
    }

    default Vector3f getFinalColor() {
        return ARGB.vector3fFromRGB24(this.getColor());
    }

    default boolean shouldRandomizeColor() {
        return false;
    }

    default float randomizeColor(float color, RandomSource randomSource) {
        return (randomSource.nextFloat() * 0.2F + 0.8F) * color * (randomSource.nextFloat() * 0.4F + 0.6F);
    }

    default boolean isEmissive() {
        return false;
    }
}