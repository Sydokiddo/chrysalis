package net.sydokiddo.chrysalis.util.technical.config;

import java.util.function.Supplier;

public class CConfigOptions {

    public static Supplier<Boolean>
        ITEM_DROPPING_SOUND = () -> true,
        SCREENSHOT_SOUND = () -> true,
        SPAWN_EGG_USE_SOUND = () -> true,
        REWORKED_MOB_GRIEFING = () -> true,
        IMPROVED_STRUCTURE_VOID_RENDERING = () -> true,
        IMPROVED_GRATE_RENDERING = () -> true,
        FIXED_ELYTRA_MODEL = () -> true,
        MOB_HEAD_SHADERS = () -> true,
        REWORKED_TOOLTIPS = () -> true,
        CHRYSALIS_TOOLTIP = () -> true,
        CHRYSALIS_CREATIVE_MODE_TAB = () -> true
    ;
}