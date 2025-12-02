package net.junebug.chrysalis.common;

import net.junebug.chrysalis.Chrysalis;

@SuppressWarnings("unused")
public class CSharedFeatures {

    /**
     * Features shared between various mods that can be enabled through said mods.
     **/

    public static boolean
        EXAMPLE_FEATURE = false,
        EXPERIENCE_BOTTLE_CHANGES = false,
        POTION_CHANGES = false,
        AMETHYST_CHANGES = false,
        ECHO_SHARD_CHANGES = false,
        ELECTROCUTED_EFFECT_SOURCES = false,
        BLIGHT_MEAL = false,
        SCOPING_STANDS = false,
        BUNDLE_BACKPORT = false,
        COPPER_GEAR_BACKPORT = false,
        NETHERITE_HORSE_ARMOR_BACKPORT = false
    ;

    public static boolean isFeatureEnabled(boolean feature) {
        return feature;
    }

    private static void sendDebugMessage(String string) {
        Chrysalis.LOGGER.info("Enabled Shared Feature: {}", string);
    }

    // region Feature Enabling

    public static void enableExampleFeature() {
        if (!EXAMPLE_FEATURE) {
            EXAMPLE_FEATURE = true;
            sendDebugMessage("Example Feature");
        }
    }

    public static void enableExperienceBottleChanges() {
        if (!EXPERIENCE_BOTTLE_CHANGES) {
            EXPERIENCE_BOTTLE_CHANGES = true;
            sendDebugMessage("Experience Bottle Changes");
        }
    }

    public static void enablePotionChanges() {
        if (!POTION_CHANGES) {
            POTION_CHANGES = true;
            sendDebugMessage("Potion Changes");
        }
    }

    public static void enableAmethystChanges() {
        if (!AMETHYST_CHANGES) {
            AMETHYST_CHANGES = true;
            sendDebugMessage("Amethyst Changes");
        }
    }


    public static void enableEchoShardChanges() {
        if (!ECHO_SHARD_CHANGES) {
            ECHO_SHARD_CHANGES = true;
            sendDebugMessage("Echo Shard Changes");
        }
    }

    public static void enableElectrocutedEffectSources() {
        if (!ELECTROCUTED_EFFECT_SOURCES) {
            ELECTROCUTED_EFFECT_SOURCES = true;
            sendDebugMessage("Electrocuted Effect Sources");
        }
    }

    public static void enableBlightMeal() {
        if (!BLIGHT_MEAL) {
            BLIGHT_MEAL = true;
            sendDebugMessage("Blight Meal");
        }
    }

    public static void enableScopingStands() {
        if (!SCOPING_STANDS) {
            SCOPING_STANDS = true;
            sendDebugMessage("Scoping Stands");
        }
    }

    public static void enableBundleBackport() {
        if (!BUNDLE_BACKPORT) {
            BUNDLE_BACKPORT = true;
            sendDebugMessage("Bundle Backport");
        }
    }

    public static void enableCopperGearBackport() {
        if (!COPPER_GEAR_BACKPORT) {
            COPPER_GEAR_BACKPORT = true;
            sendDebugMessage("Copper Gear Backport");
        }
    }

    public static void enableNetheriteHorseArmorBackport() {
        if (!NETHERITE_HORSE_ARMOR_BACKPORT) {
            NETHERITE_HORSE_ARMOR_BACKPORT = true;
            sendDebugMessage("Netherite Horse Armor Backport");
        }
    }

    // endregion
}