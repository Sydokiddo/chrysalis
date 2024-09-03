package net.sydokiddo.chrysalis.registry;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.commands.CooldownCommand;
import net.sydokiddo.chrysalis.misc.util.commands.HealCommand;
import net.sydokiddo.chrysalis.misc.util.entities.ChrysalisMemoryModules;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.misc.*;
import java.awt.*;

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
    public static GameRules.Key<GameRules.BooleanValue> RULE_PLAYER_DEATH_ITEM_DESPAWNING =
        GameRuleRegistry.register(
        "playerDeathItemDespawning",
        GameRules.Category.PLAYER,
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

    // region Colors and Fonts

    public static final Color
        CHRYSALIS_COLOR = Color.decode("#A27FFF")
    ;

    public static final MutableComponent
        CHRYSALIS_ICON = Component.translatable("gui.icon.chrysalis")
    ;

    @SuppressWarnings("unused")
    public static final ResourceLocation
        FIVE_FONT = Chrysalis.id("five"),
        FIVE_ALT_FONT = Chrysalis.id("five_alt")
    ;

    public static void setTooltipIconsFont(MutableComponent mutableComponent) {
        mutableComponent.setStyle(mutableComponent.getStyle().withFont(Chrysalis.id("tooltip_icons")));
    }

    // endregion

    // Registry

    public static void registerAll() {
        ChrysalisDamageSources.registerDamageSources();
        ChrysalisCriteriaTriggers.registerCriteriaTriggers();
        ChrysalisSoundEvents.registerSounds();
        ChrysalisDebugItems.registerDebugItems();
        ChrysalisCreativeModeTabs.registerCreativeTabs();
        ChrysalisMemoryModules.MEMORY_MODULES.register();
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> HealCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> CooldownCommand.register(commandDispatcher, commandBuildContext));
    }
}