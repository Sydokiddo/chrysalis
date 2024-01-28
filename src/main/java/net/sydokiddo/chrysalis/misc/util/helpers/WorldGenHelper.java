package net.sydokiddo.chrysalis.misc.util.helpers;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.placement.*;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class WorldGenHelper {

    /**
     * Various methods for assisting with world generation and properties relating to it.
     **/

    public static Predicate<BiomeSelectionContext> isValidBiomeForMobSpawning() {
        return context -> !context.getBiomeRegistryEntry().is(ChrysalisTags.WITHOUT_MOB_SPAWNS);
    }

    public static Predicate<BiomeSelectionContext> isOverworld() {
        return context -> context.canGenerateIn(LevelStem.OVERWORLD);
    }

    public static Predicate<BiomeSelectionContext> isNether() {
        return context -> context.canGenerateIn(LevelStem.NETHER);
    }

    public static Predicate<BiomeSelectionContext> isEnd() {
        return context -> context.canGenerateIn(LevelStem.END);
    }

    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
        return List.of(CountPlacement.of(count), InSquarePlacement.spread(), modifier, BiomeFilter.biome());
    }
}