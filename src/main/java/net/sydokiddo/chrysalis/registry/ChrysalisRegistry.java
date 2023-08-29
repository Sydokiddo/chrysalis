package net.sydokiddo.chrysalis.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.CreativeTabHelper;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.misc.*;

public class ChrysalisRegistry {

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
    public static GameRules.Key<GameRules.BooleanValue> RULE_EXPLOSION_ITEM_DECAY =
        GameRuleRegistry.register(
        "explosionItemDecay",
        GameRules.Category.DROPS,
        GameRuleFactory.createBooleanRule(true)
    );

    // Registries

    public static void registerAll() {

        ChrysalisResourcePacks.registerResourcePacks();
        RegistryHelpers.init();
        CreativeTabHelper.init();
        ChrysalisDamageSources.registerDamageSources();
        ChrysalisGameEvents.registerGameEvents();
        ChrysalisCriteriaTriggers.registerCriteriaTriggers();

        if (Chrysalis.IS_DEBUG) {
            registerDebugUtilities();
        }
    }

    private static void registerDebugUtilities() {
        ChrysalisDebugItems.registerDebugItems();
        ChrysalisCreativeModeTabs.registerCreativeTabs();
        Chrysalis.LOGGER.info("Debug environment detected! Initializing debug utilities for Chrysalis.");
    }
}