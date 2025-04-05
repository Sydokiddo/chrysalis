package net.sydokiddo.chrysalis.common.misc;

import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.util.helpers.RegistryHelper;

public class CGameRules {

    // region Game Rules

    public static GameRules.Key<GameRules.BooleanValue>
        RULE_MOB_WORLD_INTERACTIONS = RegistryHelper.registerBooleanGameRule("mobWorldInteractions", GameRules.Category.MOBS, true),
        RULE_DRAGON_GRIEFING = RegistryHelper.registerBooleanGameRule("dragonGriefing", GameRules.Category.MOBS, true),
        RULE_WITHER_GRIEFING = RegistryHelper.registerBooleanGameRule("witherGriefing", GameRules.Category.MOBS, true),
        RULE_DESTROY_ITEMS_IN_EXPLOSIONS = RegistryHelper.registerBooleanGameRule("destroyItemsInExplosions", GameRules.Category.DROPS, true),
        RULE_PLAYER_DEATH_ITEM_DESPAWNING = RegistryHelper.registerBooleanGameRule("playerDeathItemDespawning", GameRules.Category.PLAYER, true),
        RULE_DO_NETHER_PORTAL_ACTIVATING = RegistryHelper.registerBooleanGameRule("doNetherPortalActivating", GameRules.Category.UPDATES, true),
        RULE_DO_END_PORTAL_ACTIVATING = RegistryHelper.registerBooleanGameRule("doEndPortalActivating", GameRules.Category.UPDATES, true),
        RULE_BEDS_EXPLODE = RegistryHelper.registerBooleanGameRule("bedsExplode", GameRules.Category.MISC, true),
        RULE_RESPAWN_ANCHORS_EXPLODE = RegistryHelper.registerBooleanGameRule("respawnAnchorsExplode", GameRules.Category.MISC, true),
        RULE_SEND_DEBUG_UTILITY_FEEDBACK = RegistryHelper.registerBooleanGameRule("sendDebugUtilityFeedback", GameRules.Category.CHAT, true)
    ;

    // endregion

    // region Registry

    public static void register() {}

    // endregion
}