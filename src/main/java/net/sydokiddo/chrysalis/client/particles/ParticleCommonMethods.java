package net.sydokiddo.chrysalis.client.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public interface ParticleCommonMethods {

    /**
     * Common methods utilized by multiple custom particle effects.
     **/

    String
        colorString = "color",
        startingColorString = "starting_color",
        endingColorString = "ending_color",
        randomizeColorString = "randomize_color",
        emissiveString = "emissive",
        hasGravityString = "has_gravity",
        directionString = "direction",
        lifeTimeString = "life_time",
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

        int int1 = lightColor & 255;
        int int2 = lightColor >> 16 & 255;

        if ((int1 += (int) (divider * 15.0F * 16.0F)) > 240) int1 = 240;
        return int1 | int2 << 16;
    }

    default float shrinkParticle(float quadSize, float divider) {
        return quadSize * (1.0F - divider * divider * 0.5F);
    }

    default float growParticle(float quadSize, int age, float tickRate, int lifeTime) {
        return quadSize * Mth.clamp(((float) age + tickRate) / (float) lifeTime * 0.75F, 0.0F, 1.0F);
    }

    Codec<Integer> LIFE_TIME = Codec.INT.validate(
        life_time -> life_time >= 10 && life_time <= 100 ? DataResult.success(life_time) : DataResult.error(() -> "Value must be within range [10 ; 100]: " + life_time)
    );

    default int getClampedLifeTime(int lifeTime) {
        return Mth.clamp(lifeTime, 10, 100);
    }
}