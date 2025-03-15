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
        FIRE_COLOR = Color.decode("#FF6A00"),
        SOUL_FIRE_COLOR = Color.decode("#01A7AC"),
        MEMORY_FIRE_COLOR = Color.decode("#6CD86C"),
        WAXED_COLOR = Color.decode("#FABF29"),
        BLINDNESS_COLOR = Color.decode("#5C4182"),
        HASTE_COLOR = Color.decode("#FFB67F"),
        MINING_FATIGUE_COLOR = Color.decode("#7575B5"),
        WITHER_COLOR = Color.decode("#754655")
    ;

    // endregion

    // region Icons

    @SuppressWarnings("unused")
    public static final MutableComponent
        CHRYSALIS_ICON = Component.translatable("gui.icon.chrysalis.mod_icon"),
        GEAR_ICON = Component.translatable("gui.icon.chrysalis.gear"),
        WARNING_ICON = Component.translatable("gui.icon.chrysalis.warning"),
        QUESTION_MARK_ICON = Component.translatable("gui.icon.chrysalis.question_mark"),
        TOOLTIP_ICON = Component.translatable("gui.icon.chrysalis.tooltip"),
        POTION_ICON = Component.translatable("gui.icon.chrysalis.potion"),
        ARMOR_ICON = Component.translatable("gui.icon.chrysalis.armor"),
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
        SNOUT_ICON = Component.translatable("gui.icon.chrysalis.snout"),
        WAXED_ICON = Component.translatable("gui.icon.chrysalis.waxed"),
        FLAME_ICON = Component.translatable("gui.icon.chrysalis.flame"),
        SOUL_FLAME_ICON = Component.translatable("gui.icon.chrysalis.soul_flame"),
        MEMORY_FLAME_ICON = Component.translatable("gui.icon.chrysalis.memory_flame")
    ;

    // endregion

    // region Fonts

    public static final ResourceLocation
        FIVE_FONT = Chrysalis.resourceLocationId("five"),
        FIVE_ALT_FONT = Chrysalis.resourceLocationId("five_alt")
    ;

    public static final String TOOLTIP_ICONS_NAME = "tooltip_icons";

    public static void setTooltipIconsFont(MutableComponent mutableComponent, String modID) {
        mutableComponent.setStyle(mutableComponent.getStyle().withFont(ResourceLocation.fromNamespaceAndPath(modID, TOOLTIP_ICONS_NAME)));
    }

    // endregion
}