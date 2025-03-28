package net.sydokiddo.chrysalis.util.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.sydokiddo.chrysalis.Chrysalis;
import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public class ComponentHelper {

    // region Common Components

    public static final Component
        NONE = Component.translatable("gui.chrysalis.none"),
        UNKNOWN = Component.translatable("gui.chrysalis.unknown")
    ;

    public static Component getWeatherComponent(Level level, Holder<Biome> biome, BlockPos blockPos) {

        MutableComponent weatherType;

        if (level.isRainingAt(blockPos)) {
            if (level.isThundering()) weatherType = Component.translatable("gui.chrysalis.weather.thundering");
            else weatherType = Component.translatable("gui.chrysalis.weather.raining");
        } else {
            if (level.isRaining() && biome.value().getPrecipitationAt(blockPos, level.getSeaLevel()) == Biome.Precipitation.SNOW) weatherType = Component.translatable("gui.chrysalis.weather.snowing");
            else weatherType = Component.translatable("gui.chrysalis.weather.clear");
        }

        return weatherType;
    }

    public static Component getMoonPhaseComponent(Level level) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && !minecraft.level.dimensionType().hasFixedTime()) return Component.translatable("gui.chrysalis.moon_phase." + (level.getMoonPhase() + 1));
        else return NONE;
    }

    public static Component getDimensionComponent(String dimension) {
        String registryKey = dimension.split(":")[0];
        String registryPath = dimension.split(":")[1];
        return Component.translatable("dimension." + registryKey + "." + registryPath);
    }

    // endregion

    // region Colors

    public static final Color
        CHRYSALIS_COLOR = Color.decode("#A27FFF"),
        ENCHANTMENT_COLOR = Color.decode("#964CFF"),
        ENCHANTMENT_DARKER_COLOR = Color.decode("#4A1C8E"),
        CURSE_COLOR = Color.decode("#FF5555"),
        CURSE_DARKER_COLOR = Color.decode("#8E1C1C"),
        EXPERIENCE_COLOR = Color.decode("#B9E85C"),
        WAXED_COLOR = Color.decode("#FABF29"),
        REMAINS_ON_DEATH_COLOR = Color.decode("#8EB4DB"),
        FIRE_COLOR = Color.decode("#FF6A00"),
        SOUL_FIRE_COLOR = Color.decode("#01A7AC"),
        MEMORY_FIRE_COLOR = Color.decode("#6CD86C"),
        VOID_FIRE_COLOR = Color.decode("#C920D3"),
        TREACHEROUS_FIRE_COLOR = Color.decode("#FD4D4D"),
        NECROTIC_FIRE_COLOR = Color.decode("#AFB42B"),
        PURITY_FIRE_COLOR = Color.decode("#FF8A8A"),
        BLINDNESS_COLOR = Color.decode("#5C4182"),
        HASTE_COLOR = Color.decode("#FFB67F"),
        MINING_FATIGUE_COLOR = Color.decode("#7575B5"),
        WITHER_COLOR = Color.decode("#754655")
    ;

    // endregion

    // region Icons

    public static final MutableComponent
        CHRYSALIS_ICON = Component.translatable("gui.icon.chrysalis.mod_icon"),
        GEAR_ICON = Component.translatable("gui.icon.chrysalis.gear"),
        WARNING_ICON = Component.translatable("gui.icon.chrysalis.warning"),
        QUESTION_MARK_ICON = Component.translatable("gui.icon.chrysalis.question_mark"),
        STAR_ICON = Component.translatable("gui.icon.chrysalis.star"),
        TOOLTIP_ICON = Component.translatable("gui.icon.chrysalis.tooltip"),
        TOOL_ICON = Component.translatable("gui.icon.chrysalis.tool"),
        BROKEN_TOOL_ICON = Component.translatable("gui.icon.chrysalis.broken_tool"),
        SWORD_ICON = Component.translatable("gui.icon.chrysalis.sword"),
        BOW_ICON = Component.translatable("gui.icon.chrysalis.bow"),
        CROSSBOW_ICON = Component.translatable("gui.icon.chrysalis.crossbow"),
        ARROW_ICON = Component.translatable("gui.icon.chrysalis.arrow"),
        TRIDENT_ICON = Component.translatable("gui.icon.chrysalis.trident"),
        MACE_ICON = Component.translatable("gui.icon.chrysalis.mace"),
        ARMOR_ICON = Component.translatable("gui.icon.chrysalis.armor"),
        POTION_ICON = Component.translatable("gui.icon.chrysalis.potion"),
        RED_HEART_ICON = Component.translatable("gui.icon.chrysalis.red_heart"),
        ORANGE_HEART_ICON = Component.translatable("gui.icon.chrysalis.orange_heart"),
        YELLOW_HEART_ICON = Component.translatable("gui.icon.chrysalis.yellow_heart"),
        GREEN_HEART_ICON = Component.translatable("gui.icon.chrysalis.green_heart"),
        BLUE_HEART_ICON = Component.translatable("gui.icon.chrysalis.blue_heart"),
        PURPLE_HEART_ICON = Component.translatable("gui.icon.chrysalis.purple_heart"),
        PINK_HEART_ICON = Component.translatable("gui.icon.chrysalis.pink_heart"),
        WHITE_HEART_ICON = Component.translatable("gui.icon.chrysalis.white_heart"),
        GRAY_HEART_ICON = Component.translatable("gui.icon.chrysalis.gray_heart"),
        BLACK_HEART_ICON = Component.translatable("gui.icon.chrysalis.black_heart"),
        BROWN_HEART_ICON = Component.translatable("gui.icon.chrysalis.brown_heart"),
        BROKEN_HEART_ICON = Component.translatable("gui.icon.chrysalis.broken_heart"),
        HUNGER_POINT_ICON = Component.translatable("gui.icon.chrysalis.hunger_point"),
        WAXED_ICON = Component.translatable("gui.icon.chrysalis.waxed"),
        SNOUT_ICON = Component.translatable("gui.icon.chrysalis.snout"),
        FLAME_ICON = Component.translatable("gui.icon.chrysalis.flame"),
        SOUL_FLAME_ICON = Component.translatable("gui.icon.chrysalis.soul_flame"),
        MEMORY_FLAME_ICON = Component.translatable("gui.icon.chrysalis.memory_flame"),
        VOID_FLAME_ICON = Component.translatable("gui.icon.chrysalis.void_flame"),
        TREACHEROUS_FLAME_ICON = Component.translatable("gui.icon.chrysalis.treacherous_flame"),
        NECROTIC_FLAME_ICON = Component.translatable("gui.icon.chrysalis.necrotic_flame"),
        PURITY_FLAME_ICON = Component.translatable("gui.icon.chrysalis.purity_flame"),
        EXPERIENCE_ORB_ICON = Component.translatable("gui.icon.chrysalis.experience_orb"),
        CURSED_EXPERIENCE_ORB_ICON = Component.translatable("gui.icon.chrysalis.cursed_experience_orb"),
        REDSTONE_DUST_ICON = Component.translatable("gui.icon.chrysalis.redstone_dust"),
        COPPER_INGOT_ICON = Component.translatable("gui.icon.chrysalis.copper_ingot"),
        RESIN_BRICK_ICON = Component.translatable("gui.icon.chrysalis.resin_brick"),
        GOLD_INGOT_ICON = Component.translatable("gui.icon.chrysalis.gold_ingot"),
        GLOWSTONE_DUST_ICON = Component.translatable("gui.icon.chrysalis.glowstone_dust"),
        EMERALD_ICON = Component.translatable("gui.icon.chrysalis.emerald"),
        PRISMARINE_CRYSTALS_ICON = Component.translatable("gui.icon.chrysalis.prismarine_crystals"),
        DIAMOND_ICON = Component.translatable("gui.icon.chrysalis.diamond"),
        LAPIS_LAZULI_ICON = Component.translatable("gui.icon.chrysalis.lapis_lazuli"),
        ECHO_SHARD_ICON = Component.translatable("gui.icon.chrysalis.echo_shard"),
        AMETHYST_SHARD_ICON = Component.translatable("gui.icon.chrysalis.amethyst_shard"),
        NETHER_QUARTZ_ICON = Component.translatable("gui.icon.chrysalis.nether_quartz"),
        IRON_INGOT_ICON = Component.translatable("gui.icon.chrysalis.iron_ingot"),
        COAL_ICON = Component.translatable("gui.icon.chrysalis.coal"),
        NETHERITE_INGOT_ICON = Component.translatable("gui.icon.chrysalis.netherite_ingot"),
        NETHERITE_SCRAP_ICON = Component.translatable("gui.icon.chrysalis.netherite_scrap"),
        REMAINS_ON_DEATH_ICON = Component.translatable("gui.icon.chrysalis.remains_on_death"),
        EGG_ICON = Component.translatable("gui.icon.chrysalis.egg")
    ;

    // endregion

    // region Fonts

    public static final ResourceLocation
        FIVE_FONT = Chrysalis.resourceLocationId("five"),
        FIVE_ALT_FONT = Chrysalis.resourceLocationId("five_alt"),
        FIVE_LOWERED_FONT = Chrysalis.resourceLocationId("five_lowered"),
        FIVE_ALT_LOWERED_FONT = Chrysalis.resourceLocationId("five_alt_lowered"),
        GALACTIC_ALT_FONT = Chrysalis.resourceLocationId("galactic_alt")
    ;

    public static void setIconsFont(MutableComponent mutableComponent, String modID) {
        mutableComponent.setStyle(mutableComponent.getStyle().withFont(ResourceLocation.fromNamespaceAndPath(modID, "icons")));
    }

    // endregion

    // region Name Lists

    public static final List<String> SOUL_FIRE_NAMES = List.of(
        "soul",
        "Soul",
        "sculk",
        "Sculk",
        "warden",
        "Warden",
        "banshee",
        "Banshee"
    );

    public static final List<String> MEMORY_FIRE_NAMES = List.of(
        "memory",
        "Memory",
        "memories",
        "Memories",
        "kaleidoscope",
        "Kaleidoscope",
        "ethereal",
        "Ethereal",
        "everlasting",
        "Everlasting",
        "ichor",
        "Ichor"
    );

    public static final List<String> VOID_FIRE_NAMES = List.of(
        "void",
        "Void",
        "corrupt",
        "Corrupt",
        "ender",
        "Ender",
        "endless",
        "Endless"
    );

    public static final List<String> TREACHEROUS_FIRE_NAMES = List.of(
        "treacherous",
        "Treacherous",
        "treachery",
        "Treachery",
        "sanctum",
        "Sanctum",
        "carcass",
        "Carcass"
    );

    public static final List<String> NECROTIC_FIRE_NAMES = List.of(
        "necro",
        "Necro",
        "undead",
        "Undead",
        "zombie",
        "Zombie",
        "blight",
        "Blight"
    );

    public static final List<String> PURITY_FIRE_NAMES = List.of(
        "purity",
        "Purity",
        "purify",
        "Purify",
        "purified",
        "Purified",
        "purification",
        "Purification",
        "gumpkin",
        "Gumpkin",
        "hominid",
        "Hominid"
    );

    // endregion
}