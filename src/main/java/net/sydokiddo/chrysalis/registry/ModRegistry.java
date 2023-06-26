package net.sydokiddo.chrysalis.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.CreativeTabHelper;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.chrysalis.registry.items.DebugItems;
import net.sydokiddo.chrysalis.registry.misc.ModCreativeModeTabs;
import net.sydokiddo.chrysalis.registry.misc.ModDamageSources;
import net.sydokiddo.chrysalis.registry.misc.ModResourcePacks;

public class ModRegistry {

    // Game Rules

    public static GameRules.Key<GameRules.BooleanValue> RULE_PASSIVE_GRIEFING =
        GameRuleRegistry.register(
        "passiveGriefing",
        GameRules.Category.MOBS,
        GameRuleFactory.createBooleanRule(true)
    );
    public static GameRules.Key<GameRules.BooleanValue> RULE_DRAGON_GRIEFING =
        GameRuleRegistry.register(
        "dragonGriefing",
        GameRules.Category.MOBS,
        GameRuleFactory.createBooleanRule(true)
    );
    public static GameRules.Key<GameRules.BooleanValue> RULE_WITHER_GRIEFING =
        GameRuleRegistry.register(
        "witherGriefing",
        GameRules.Category.MOBS,
        GameRuleFactory.createBooleanRule(true)
    );

    // Registries

    public static void registerAll() {

        ModResourcePacks.registerResourcePacks();
        RegistryHelpers.init();
        CreativeTabHelper.init();
        ModDamageSources.registerDamageSources();

        if (Chrysalis.IS_DEBUG) {
            registerDebugUtilities();
        }
    }

    private static void registerDebugUtilities() {
        DebugItems.registerDebugItems();
        ModCreativeModeTabs.registerCreativeTabs();
        Chrysalis.LOGGER.info("Debug environment detected! Initializing debug utilities for Chrysalis.");
    }
}
