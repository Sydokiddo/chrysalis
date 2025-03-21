package net.sydokiddo.chrysalis.util.helpers;

import net.minecraft.util.RandomSource;

@SuppressWarnings("unused")
public class MathHelper {

    public static float getFloatBetween(RandomSource randomSource, float min, float max) {
        if (min >= max) throw new IllegalArgumentException("bound - origin is non positive");
        return min + randomSource.nextFloat() * (max - min);
    }
}