package net.sydokiddo.chrysalis.registry.misc;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class ChrysalisGameRules {

    public static GameRules.Key<GameRules.BooleanValue>
        RULE_MOB_WORLD_INTERACTIONS = registerBooleanGameRule("mobWorldInteractions", GameRules.Category.MOBS, true),
        RULE_DRAGON_GRIEFING = registerBooleanGameRule("dragonGriefing", GameRules.Category.MOBS, true),
        RULE_WITHER_GRIEFING = registerBooleanGameRule("witherGriefing", GameRules.Category.MOBS, true),
        RULE_DESTROY_ITEMS_IN_EXPLOSIONS = registerBooleanGameRule("destroyItemsInExplosions", GameRules.Category.DROPS, true),
        RULE_PLAYER_DEATH_ITEM_DESPAWNING = registerBooleanGameRule("playerDeathItemDespawning", GameRules.Category.PLAYER, true),
        RULE_DO_NETHER_PORTAL_ACTIVATING = registerBooleanGameRule("doNetherPortalActivating", GameRules.Category.UPDATES, true),
        RULE_DO_END_PORTAL_ACTIVATING = registerBooleanGameRule("doEndPortalActivating", GameRules.Category.UPDATES, true),
        RULE_BEDS_EXPLODE = registerBooleanGameRule("bedsExplode", GameRules.Category.MISC, true),
        RULE_RESPAWN_ANCHORS_EXPLODE = registerBooleanGameRule("respawnAnchorsExplode", GameRules.Category.MISC, true),
        RULE_SEND_DEBUG_UTILITY_FEEDBACK = registerBooleanGameRule("sendDebugUtilityFeedback", GameRules.Category.CHAT, true)
    ;

    @SuppressWarnings("all")
    private static GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRuleRegistry.register(name, category, GameRuleFactory.createBooleanRule(defaultValue));
    }

    @SuppressWarnings("unused")
    private static GameRules.Key<GameRules.IntegerValue> registerIntegerGameRule(String name, GameRules.Category category, int defaultValue) {
        return GameRuleRegistry.register(name, category, GameRuleFactory.createIntRule(defaultValue));
    }
}