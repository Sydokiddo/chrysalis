package net.sydokiddo.chrysalis.common.misc;

import net.minecraft.world.level.GameRules;

public class ChrysalisGameRules {

    // region Game Rules

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

    // endregion

    // region Registry

    @SuppressWarnings("all")
    private static GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRules.register(name, category, GameRules.BooleanValue.create(defaultValue));
    }

    @SuppressWarnings("unused")
    private static GameRules.Key<GameRules.IntegerValue> registerIntegerGameRule(String name, GameRules.Category category, int defaultValue) {
        return GameRules.register(name, category, GameRules.IntegerValue.create(defaultValue));
    }

    public static void register() {}

    // endregion
}