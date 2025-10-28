package net.junebug.chrysalis.util.helpers;

import net.minecraft.util.RandomSource;

@SuppressWarnings("unused")
public class MathHelper {

    /**
     * Chooses a random float value between two given float values.
     **/

    public static float getFloatBetween(RandomSource randomSource, float min, float max) {
        if (min >= max) throw new IllegalArgumentException("bound - origin is non positive");
        return min + randomSource.nextFloat() * (max - min);
    }
}