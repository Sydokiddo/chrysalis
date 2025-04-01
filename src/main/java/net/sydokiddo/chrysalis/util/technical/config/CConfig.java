package net.sydokiddo.chrysalis.util.technical.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CConfig {

    public static final CConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private CConfig(ModConfigSpec.Builder builder) {
        CConfigOptions.ITEM_DROPPING_SOUND = builder.define("item_dropping_sound", true);
        CConfigOptions.SCREENSHOT_SOUND = builder.define("screenshot_sound", true);
        CConfigOptions.SPAWN_EGG_USE_SOUND = builder.define("spawn_egg_use_sound", true);
        CConfigOptions.REWORKED_MOB_GRIEFING = builder.define("reworked_mob_griefing", true);
        CConfigOptions.IMPROVED_STRUCTURE_VOID_RENDERING = builder.define("improved_structure_void_rendering", true);
        CConfigOptions.IMPROVED_GRATE_RENDERING = builder.define("improved_grate_rendering", true);
        CConfigOptions.FIXED_ELYTRA_MODEL = builder.define("fixed_elytra_model", true);
        CConfigOptions.MOB_HEAD_SHADERS = builder.define("mob_head_shaders", true);
        CConfigOptions.REWORKED_TOOLTIPS = builder.define("reworked_tooltips", true);
        CConfigOptions.CHRYSALIS_TOOLTIP = builder.define("chrysalis_tooltip", true);
        CConfigOptions.CHRYSALIS_CREATIVE_MODE_TAB = builder.define("chrysalis_creative_mode_tab", true);
    }

    static {
        Pair<CConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(CConfig::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }
}