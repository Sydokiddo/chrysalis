package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public interface ParticleCommonMethods {

    String
        colorString = "color",
        startingColorString = "starting_color",
        endingColorString = "ending_color",
        randomizeColorString = "randomize_color",
        emissiveString = "emissive",
        hasGravityString = "has_gravity",
        directionString = "direction",
        scaleString = "scale"
    ;

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

    default int fadeLightColor(float startingBrightness, float endingBrightness, int age, int lifeTime, int lightColor) {

        float divider = ((float) age) / (float) lifeTime;
        divider = Mth.clamp(divider, startingBrightness, endingBrightness);

        int int1 = lightColor & 0xFF;
        int int2 = lightColor >> 16 & 0xFF;

        if ((int1 += (int) (divider * 15.0F * 16.0F)) > 240) int1 = 240;
        return int1 | int2 << 16;
    }

    default float shrinkParticle(float quadSize, float divider) {
        return quadSize * (1.0F - divider * divider * 0.5F);
    }

    default float growParticle(float quadSize, int age, float tickRate, int lifeTime) {
        return quadSize * Mth.clamp(((float) age + tickRate) / (float) lifeTime * 0.75F, 0.0F, 1.0F);
    }
}