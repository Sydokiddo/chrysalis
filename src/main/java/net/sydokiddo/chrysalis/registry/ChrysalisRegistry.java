package net.sydokiddo.chrysalis.registry;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.CreativeTabHelper;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.misc.*;

@SuppressWarnings("unused")
public class ChrysalisRegistry {

    // region Game Rules

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
    public static GameRules.Key<GameRules.BooleanValue> RULE_DESTROY_ITEMS_IN_EXPLOSIONS =
        GameRuleRegistry.register(
        "destroyItemsInExplosions",
        GameRules.Category.DROPS,
        GameRuleFactory.createBooleanRule(true)
    );
    public static GameRules.Key<GameRules.BooleanValue> RULE_DO_NETHER_PORTAL_ACTIVATING =
        GameRuleRegistry.register(
        "doNetherPortalActivating",
        GameRules.Category.UPDATES,
        GameRuleFactory.createBooleanRule(true)
    );
    public static GameRules.Key<GameRules.BooleanValue> RULE_DO_END_PORTAL_ACTIVATING =
        GameRuleRegistry.register(
        "doEndPortalActivating",
        GameRules.Category.UPDATES,
        GameRuleFactory.createBooleanRule(true)
    );

    // endregion

    // Registry

    public static void registerAll() {

        RegistryHelpers.init();
        CreativeTabHelper.init();
        ChrysalisDamageSources.registerDamageSources();
        ChrysalisCriteriaTriggers.registerCriteriaTriggers();
        ChrysalisSoundEvents.registerSounds();

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