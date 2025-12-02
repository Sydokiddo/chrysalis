package net.junebug.chrysalis.common;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import java.util.function.Supplier;

public class CConfig {

    /**
     * The base config options for chrysalis.
     **/

    public static Supplier<Boolean>

        // region Server-Side

        CHANGE_DEFAULT_VAULT_KEY = () -> true,
        MOB_HEAD_SHADERS = () -> true,
        REMOVE_TOOL_COMBAT_PENALTY = () -> true,
        MODIFIED_EFFECT_COLORS = () -> true,

        REWORKED_MOB_GRIEFING = () -> true,
        TELEGRAPHED_ZOMBIE_VILLAGER_CURING = () -> true,
        DISMOUNTING_MOBS_FROM_VEHICLES = () -> true,
        EARTHQUAKES_INTERACTING_WITH_BLOCKS = () -> true,
        EARTHQUAKES_BREAKING_BLOCKS = () -> true,
        EARTHQUAKES_SPREADING_FIRE = () -> true,

        ITEM_DROPPING_SOUND = () -> true,
        SPAWN_EGG_USE_SOUND = () -> true,
        CAKE_EATING_SOUND = () -> true,

        // endregion

        // region Client-Side

        FIXED_ELYTRA_MODEL = () -> true,
        NAME_TAG_HIDING = () -> true,

        RENDER_KEY_GOLEMS_IN_FIRST_PERSON = () -> true,

        SCREENSHOT_SOUND = () -> true,
        CREATIVE_MODE_ITEM_DELETING_SOUND = () -> true,

        IMPROVED_STRUCTURE_VOID_RENDERING = () -> true,
        IMPROVED_GLASS_RENDERING = () -> true,
        IMPROVED_GRATE_RENDERING = () -> true,
        RENDER_HUNGER_BAR_WHILE_RIDING_MOBS = () -> true,

        REWORKED_TOOLTIPS = () -> true,
        CHRYSALIS_TOOLTIP = () -> false,
        CHRYSALIS_CREATIVE_MODE_TAB = () -> true,
        ADD_CHRYSALIS_ITEMS_TO_VANILLA_TABS = () -> true,
        EXPERIMENTAL_WORLD_WARNING = () -> true,
        SPLASH_TEXT_REFRESHING = () -> true

        // endregion
    ;

    public static class CConfigBuilder {

        /**
         * The builder class for chrysalis's config.
         **/

        public static final CConfig.CConfigBuilder CONFIG;
        public static final ModConfigSpec CONFIG_SPEC;

        private CConfigBuilder(ModConfigSpec.Builder builder) {

            builder.push("server_side");

                builder.push("items");
                CConfig.CHANGE_DEFAULT_VAULT_KEY = builder.gameRestart().define("change_default_vault_key", true);
                CConfig.MOB_HEAD_SHADERS = builder.define("mob_head_shaders", true);
                CConfig.REMOVE_TOOL_COMBAT_PENALTY = builder.define("remove_tool_combat_penalty", true);
                CConfig.MODIFIED_EFFECT_COLORS = builder.worldRestart().define("modified_effect_colors", true);
                builder.pop();

                builder.push("entities");
                CConfig.REWORKED_MOB_GRIEFING = builder.define("reworked_mob_griefing", true);
                CConfig.TELEGRAPHED_ZOMBIE_VILLAGER_CURING = builder.define("telegraphed_zombie_villager_curing", true);
                CConfig.DISMOUNTING_MOBS_FROM_VEHICLES = builder.define("dismounting_mobs_from_vehicles", true);
                CConfig.EARTHQUAKES_INTERACTING_WITH_BLOCKS = builder.define("earthquakes_interacting_with_blocks", true);
                CConfig.EARTHQUAKES_BREAKING_BLOCKS = builder.define("earthquakes_breaking_blocks", true);
                CConfig.EARTHQUAKES_SPREADING_FIRE = builder.define("earthquakes_spreading_fire", true);
                builder.pop();

                builder.push("sounds");
                CConfig.ITEM_DROPPING_SOUND = builder.define("item_dropping_sound", true);
                CConfig.SPAWN_EGG_USE_SOUND = builder.define("spawn_egg_use_sound", true);
                CConfig.CAKE_EATING_SOUND = builder.define("cake_eating_sound", true);
                builder.pop();

            builder.pop();

            builder.push("client_side");

                builder.push("items");
                CConfig.FIXED_ELYTRA_MODEL = builder.gameRestart().define("fixed_elytra_model", true);
                CConfig.NAME_TAG_HIDING = builder.define("name_tag_hiding", true);
                builder.pop();

                builder.push("entities");
                CConfig.RENDER_KEY_GOLEMS_IN_FIRST_PERSON = builder.define("render_key_golems_in_first_person", true);
                builder.pop();

                builder.push("sounds");
                CConfig.SCREENSHOT_SOUND = builder.define("screenshot_sound", true);
                CConfig.CREATIVE_MODE_ITEM_DELETING_SOUND = builder.define("creative_mode_item_deleting_sound", true);
                builder.pop();

                builder.push("rendering");
                CConfig.IMPROVED_STRUCTURE_VOID_RENDERING = builder.define("improved_structure_void_rendering", true);
                CConfig.IMPROVED_GLASS_RENDERING = builder.worldRestart().define("improved_glass_rendering", true);
                CConfig.IMPROVED_GRATE_RENDERING = builder.worldRestart().define("improved_grate_rendering", true);
                CConfig.RENDER_HUNGER_BAR_WHILE_RIDING_MOBS = builder.define("render_hunger_bar_while_riding_mobs", true);
                builder.pop();

                builder.push("technical");
                CConfig.REWORKED_TOOLTIPS = builder.define("reworked_tooltips", true);
                CConfig.CHRYSALIS_TOOLTIP = builder.define("chrysalis_tooltip", true);
                CConfig.CHRYSALIS_CREATIVE_MODE_TAB = builder.worldRestart().define("chrysalis_creative_mode_tab", true);
                CConfig.ADD_CHRYSALIS_ITEMS_TO_VANILLA_TABS = builder.worldRestart().define("add_chrysalis_items_to_vanilla_tabs", true); // Test
                CConfig.EXPERIMENTAL_WORLD_WARNING = builder.worldRestart().define("experimental_world_warning", true);
                CConfig.SPLASH_TEXT_REFRESHING = builder.define("splash_text_refreshing", true);
                builder.pop();

            builder.pop();
        }

        static {
            Pair<CConfig.CConfigBuilder, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(CConfig.CConfigBuilder::new);
            CONFIG = pair.getLeft();
            CONFIG_SPEC = pair.getRight();
        }
    }
}