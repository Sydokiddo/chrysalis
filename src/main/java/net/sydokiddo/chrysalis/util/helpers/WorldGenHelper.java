package net.sydokiddo.chrysalis.util.helpers;

import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.sydokiddo.chrysalis.common.misc.CTags;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class WorldGenHelper {

    /**
     * Various methods for assisting with world generation and properties relating to it.
     **/

    public static Predicate<Holder<Biome>> canMobsSpawn() {
        return context -> !context.is(CTags.WITHOUT_MOB_SPAWNS);
    }

    public static Predicate<Holder<Biome>> isOverworld() {
        return context -> !context.is(BiomeTags.IS_OVERWORLD);
    }

    public static Predicate<Holder<Biome>> isNether() {
        return context -> !context.is(BiomeTags.IS_NETHER);
    }

    public static Predicate<Holder<Biome>> isEnd() {
        return context -> !context.is(BiomeTags.IS_END);
    }

    public static boolean isNetherOrEnd(Level level) {
        return level != null && (level.dimension() == Level.NETHER || level.dimension() == Level.END);
    }
}