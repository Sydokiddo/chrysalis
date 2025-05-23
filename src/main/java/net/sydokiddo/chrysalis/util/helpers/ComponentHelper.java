package net.sydokiddo.chrysalis.util.helpers;

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

    public static final String
        noneString = "none",
        nullString = "null",
        trueString = "true",
        falseString = "false",
        enabledString = "enabled"
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
        if (!level.dimensionType().hasFixedTime()) return Component.translatable("gui.chrysalis.moon_phase." + (level.getMoonPhase() + 1));
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
        REDSTONE_COLOR = Color.decode("#971607"),
        COPPER_COLOR = Color.decode("#B4684D"),
        RESIN_COLOR = Color.decode("#FC7812"),
        GOLD_COLOR = Color.decode("#DEB12D"),
        GLOWSTONE_COLOR = Color.decode("#FFBC5E"),
        EMERALD_COLOR = Color.decode("#11A036"),
        PRISMARINE_COLOR = Color.decode("#91C5B7"),
        DIAMOND_COLOR = Color.decode("#6EECD2"),
        LAPIS_COLOR = Color.decode("#416E97"),
        AMETHYST_COLOR = Color.decode("#9A5CC6"),
        QUARTZ_COLOR = Color.decode("#E3D4C4"),
        IRON_COLOR = Color.decode("#ECECEC"),
        COAL_COLOR = Color.decode("#393E46"),
        NETHERITE_COLOR = Color.decode("#625859"),
        NETHERITE_SCRAP_COLOR = Color.decode("#7E6059"),
        BLINDNESS_COLOR = Color.decode("#5C4182"),
        HASTE_COLOR = Color.decode("#FFB67F"),
        MINING_FATIGUE_COLOR = Color.decode("#7575B5"),
        WITHER_COLOR = Color.decode("#754655")
    ;

    // endregion

    // region Icons

    public static final MutableComponent
        CHRYSALIS_ICON = Component.translatable("gui.chrysalis.icon.mod_icon"),
        GEAR_ICON = Component.translatable("gui.chrysalis.icon.gear"),
        WARNING_ICON = Component.translatable("gui.chrysalis.icon.warning"),
        QUESTION_MARK_ICON = Component.translatable("gui.chrysalis.icon.question_mark"),
        STAR_ICON = Component.translatable("gui.chrysalis.icon.star"),
        TOOLTIP_ICON = Component.translatable("gui.chrysalis.icon.tooltip"),
        TOOL_ICON = Component.translatable("gui.chrysalis.icon.tool"),
        BROKEN_TOOL_ICON = Component.translatable("gui.chrysalis.icon.broken_tool"),
        SWORD_ICON = Component.translatable("gui.chrysalis.icon.sword"),
        BOW_ICON = Component.translatable("gui.chrysalis.icon.bow"),
        CROSSBOW_ICON = Component.translatable("gui.chrysalis.icon.crossbow"),
        ARROW_ICON = Component.translatable("gui.chrysalis.icon.arrow"),
        TRIDENT_ICON = Component.translatable("gui.chrysalis.icon.trident"),
        MACE_ICON = Component.translatable("gui.chrysalis.icon.mace"),
        ARMOR_ICON = Component.translatable("gui.chrysalis.icon.armor"),
        POTION_ICON = Component.translatable("gui.chrysalis.icon.potion"),
        RED_HEART_ICON = Component.translatable("gui.chrysalis.icon.red_heart"),
        ORANGE_HEART_ICON = Component.translatable("gui.chrysalis.icon.orange_heart"),
        YELLOW_HEART_ICON = Component.translatable("gui.chrysalis.icon.yellow_heart"),
        GREEN_HEART_ICON = Component.translatable("gui.chrysalis.icon.green_heart"),
        BLUE_HEART_ICON = Component.translatable("gui.chrysalis.icon.blue_heart"),
        PURPLE_HEART_ICON = Component.translatable("gui.chrysalis.icon.purple_heart"),
        PINK_HEART_ICON = Component.translatable("gui.chrysalis.icon.pink_heart"),
        WHITE_HEART_ICON = Component.translatable("gui.chrysalis.icon.white_heart"),
        GRAY_HEART_ICON = Component.translatable("gui.chrysalis.icon.gray_heart"),
        BLACK_HEART_ICON = Component.translatable("gui.chrysalis.icon.black_heart"),
        BROWN_HEART_ICON = Component.translatable("gui.chrysalis.icon.brown_heart"),
        BROKEN_HEART_ICON = Component.translatable("gui.chrysalis.icon.broken_heart"),
        HUNGER_POINT_ICON = Component.translatable("gui.chrysalis.icon.hunger_point"),
        WAXED_ICON = Component.translatable("gui.chrysalis.icon.waxed"),
        SNOUT_ICON = Component.translatable("gui.chrysalis.icon.snout"),
        FLAME_ICON = Component.translatable("gui.chrysalis.icon.flame"),
        SOUL_FLAME_ICON = Component.translatable("gui.chrysalis.icon.soul_flame"),
        MEMORY_FLAME_ICON = Component.translatable("gui.chrysalis.icon.memory_flame"),
        VOID_FLAME_ICON = Component.translatable("gui.chrysalis.icon.void_flame"),
        TREACHEROUS_FLAME_ICON = Component.translatable("gui.chrysalis.icon.treacherous_flame"),
        NECROTIC_FLAME_ICON = Component.translatable("gui.chrysalis.icon.necrotic_flame"),
        PURITY_FLAME_ICON = Component.translatable("gui.chrysalis.icon.purity_flame"),
        EXPERIENCE_ORB_ICON = Component.translatable("gui.chrysalis.icon.experience_orb"),
        CURSED_EXPERIENCE_ORB_ICON = Component.translatable("gui.chrysalis.icon.cursed_experience_orb"),
        REDSTONE_DUST_ICON = Component.translatable("gui.chrysalis.icon.redstone_dust"),
        COPPER_INGOT_ICON = Component.translatable("gui.chrysalis.icon.copper_ingot"),
        RESIN_BRICK_ICON = Component.translatable("gui.chrysalis.icon.resin_brick"),
        GOLD_INGOT_ICON = Component.translatable("gui.chrysalis.icon.gold_ingot"),
        GLOWSTONE_DUST_ICON = Component.translatable("gui.chrysalis.icon.glowstone_dust"),
        EMERALD_ICON = Component.translatable("gui.chrysalis.icon.emerald"),
        PRISMARINE_CRYSTALS_ICON = Component.translatable("gui.chrysalis.icon.prismarine_crystals"),
        DIAMOND_ICON = Component.translatable("gui.chrysalis.icon.diamond"),
        LAPIS_LAZULI_ICON = Component.translatable("gui.chrysalis.icon.lapis_lazuli"),
        ECHO_SHARD_ICON = Component.translatable("gui.chrysalis.icon.echo_shard"),
        AMETHYST_SHARD_ICON = Component.translatable("gui.chrysalis.icon.amethyst_shard"),
        NETHER_QUARTZ_ICON = Component.translatable("gui.chrysalis.icon.nether_quartz"),
        IRON_INGOT_ICON = Component.translatable("gui.chrysalis.icon.iron_ingot"),
        COAL_ICON = Component.translatable("gui.chrysalis.icon.coal"),
        NETHERITE_INGOT_ICON = Component.translatable("gui.chrysalis.icon.netherite_ingot"),
        NETHERITE_SCRAP_ICON = Component.translatable("gui.chrysalis.icon.netherite_scrap"),
        WATER_ICON = Component.translatable("gui.chrysalis.icon.water"),
        LAVA_ICON = Component.translatable("gui.chrysalis.icon.lava"),
        FLOWSLUDGE_ICON = Component.translatable("gui.chrysalis.icon.flowsludge"),
        MOLTEN_VOID_ICON = Component.translatable("gui.chrysalis.icon.molten_void"),
        ECTOPLASM_ICON = Component.translatable("gui.chrysalis.icon.ectoplasm"),
        REMAINS_ON_DEATH_ICON = Component.translatable("gui.chrysalis.icon.remains_on_death"),
        VIBRATION_ICON = Component.translatable("gui.chrysalis.icon.vibration"),
        MUFFLED_ICON = Component.translatable("gui.chrysalis.icon.muffled"),
        MUSIC_NOTE_ICON = Component.translatable("gui.chrysalis.icon.music_note"),
        EGG_ICON = Component.translatable("gui.chrysalis.icon.egg")
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
        "allay",
        "Allay",
        "vex",
        "Vex",
        "wisp",
        "Wisp",
        "apparition",
        "Apparition",
        "banshee",
        "Banshee",
        "ecto",
        "Ecto",
        "ghost",
        "Ghost"
    );

    public static final List<String> MEMORY_FIRE_NAMES = List.of(
        "memory",
        "Memory",
        "memories",
        "Memories",
        "remember",
        "Remember",
        "kaleidoscope",
        "Kaleidoscope",
        "ethereal",
        "Ethereal",
        "everlasting",
        "Everlasting",
        "experience",
        "Experience",
        "knowledge",
        "Knowledge",
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