package net.junebug.chrysalis.util.technical.config;

import java.util.function.Supplier;

public class CConfigOptions {

    public static Supplier<Boolean>
        ITEM_DROPPING_SOUND = () -> true,
        SCREENSHOT_SOUND = () -> true,
        SPAWN_EGG_USE_SOUND = () -> true,
        CREATIVE_MODE_ITEM_DELETING_SOUNDS = () -> true,
        REWORKED_MOB_GRIEFING = () -> true,
        TELEGRAPHED_ZOMBIE_VILLAGER_CURING = () -> true,
        IMPROVED_STRUCTURE_VOID_RENDERING = () -> true,
        IMPROVED_GLASS_RENDERING = () -> true,
        IMPROVED_GRATE_RENDERING = () -> true,
        CHANGE_DEFAULT_VAULT_KEY = () -> true,
        FIXED_ELYTRA_MODEL = () -> true,
        MOB_HEAD_SHADERS = () -> true,
        NAME_TAG_HIDING = () -> true,
        REWORKED_TOOLTIPS = () -> true,
        CHRYSALIS_TOOLTIP = () -> true,
        CHRYSALIS_CREATIVE_MODE_TAB = () -> true,
        EXPERIMENTAL_WORLD_WARNING = () -> true
    ;
}