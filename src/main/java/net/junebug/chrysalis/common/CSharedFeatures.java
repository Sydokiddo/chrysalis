package net.junebug.chrysalis.common;

import net.junebug.chrysalis.Chrysalis;

@SuppressWarnings("unused")
public class CSharedFeatures {

    private static boolean
        EXAMPLE_FEATURE = false,
        EXPERIENCE_BOTTLE_REWORK = false,
        ECHO_SHARD_REWORK = false,
        BUNDLE_BACKPORT = false,
        COPPER_GEAR_BACKPORT = false,
        NETHERITE_HORSE_ARMOR_BACKPORT = false
    ;

    private static void sendDebugMessage(String string) {
        Chrysalis.LOGGER.info("Enabled Shared Feature: {}", string);
    }

    public static void enableExampleFeature() {
        if (!EXAMPLE_FEATURE) {
            EXAMPLE_FEATURE = true;
            sendDebugMessage("Example Feature");
        }
    }

    public static void enableExperienceBottleRework() {
        if (!EXPERIENCE_BOTTLE_REWORK) {
            EXPERIENCE_BOTTLE_REWORK = true;
            sendDebugMessage("Experience Bottle Rework");
        }
    }

    public static void enableEchoShardRework() {
        if (!ECHO_SHARD_REWORK) {
            ECHO_SHARD_REWORK = true;
            sendDebugMessage("Echo Shard Rework");
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
}