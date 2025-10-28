package net.junebug.chrysalis.common.misc;

import net.minecraft.world.level.GameRules;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.util.helpers.RegistryHelper;

public class CGameRules {

    // region Game Rules

    public static GameRules.Key<GameRules.BooleanValue>
        RULE_MOB_WORLD_INTERACTIONS = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("mobWorldInteractions"), GameRules.Category.MOBS, true),
        RULE_DRAGON_GRIEFING = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("dragonGriefing"), GameRules.Category.MOBS, true),
        RULE_WITHER_GRIEFING = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("witherGriefing"), GameRules.Category.MOBS, true),
        RULE_DESTROY_ITEMS_IN_EXPLOSIONS = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("destroyItemsInExplosions"), GameRules.Category.DROPS, true),
        RULE_PLAYER_DEATH_ITEM_DESPAWNING = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("playerDeathItemDespawning"), GameRules.Category.PLAYER, true),
        RULE_ITEM_COOLDOWNS = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("itemCooldowns"), GameRules.Category.PLAYER, true),
        RULE_SEND_DEBUG_UTILITY_FEEDBACK = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("sendDebugUtilityFeedback"), GameRules.Category.CHAT, true),
        RULE_DO_NETHER_PORTAL_ACTIVATING = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("doNetherPortalActivating"), GameRules.Category.UPDATES, true),
        RULE_DO_END_PORTAL_ACTIVATING = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("doEndPortalActivating"), GameRules.Category.UPDATES, true),
        RULE_BEDS_EXPLODE = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("bedsExplode"), GameRules.Category.MISC, true),
        RULE_RESPAWN_ANCHORS_EXPLODE = RegistryHelper.registerBooleanGameRule(Chrysalis.stringId("respawnAnchorsExplode"), GameRules.Category.MISC, true)
    ;

    // endregion

    // region Registry

    public static void register() {}

    // endregion
}