package net.sydokiddo.chrysalis.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.commands.CooldownCommand;
import net.sydokiddo.chrysalis.misc.util.commands.HealCommand;
import net.sydokiddo.chrysalis.misc.util.entities.ChrysalisMemoryModules;
import net.sydokiddo.chrysalis.misc.util.music.StructureChangedPayload;
import net.sydokiddo.chrysalis.misc.util.music.StructureMusic;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.misc.*;
import org.lwjgl.glfw.GLFW;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

    // region Miscellaneous

    public static final KeyMapping PANORAMIC_SCREENSHOT_KEY = new KeyMapping("key.chrysalis.panoramic_screenshot", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F4, "key.categories.misc");

    // endregion

    // Registry

    // region Structure Music Registry

    public static Map<String, StructureMusicSound> registeredStructures = new HashMap<>();

    public static void registerStructureMusic(String structureName, String soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        registeredStructures.put(structureName, new StructureMusicSound(soundEvent, minDelay, maxDelay, replaceCurrentMusic));
    }

    public record StructureMusicSound(String name, int minDelay, int maxDelay, boolean replaceCurrentMusic) {}

    // endregion

    public static void registerAll() {

        // region Base Registries

        ChrysalisDamageSources.registerDamageSources();
        ChrysalisCriteriaTriggers.registerCriteriaTriggers();
        ChrysalisSoundEvents.registerSounds();
        ChrysalisSoundEvents.registerStructureMusic();
        ChrysalisDebugItems.registerDebugItems();
        ChrysalisCreativeModeTabs.registerCreativeTabs();
        ChrysalisMemoryModules.MEMORY_MODULES.register();

        // endregion

        // region Commands

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> HealCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> CooldownCommand.register(commandDispatcher, commandBuildContext));

        // endregion

        // region Panoramic Screenshots

        KeyBindingHelper.registerKeyBinding(PANORAMIC_SCREENSHOT_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (PANORAMIC_SCREENSHOT_KEY.consumeClick()) {

                client.grabPanoramixScreenshot(new File(FabricLoader.getInstance().getGameDir().toString()), 1024, 1024);

                Component panoramaTakenText = Component.literal("panorama_0.png – panorama_5.png").withStyle(ChatFormatting.UNDERLINE)
                .withStyle((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, new File(FabricLoader.getInstance().getGameDir().toString() + "/screenshots/").getAbsolutePath())));

                Component message = Component.translatable("screenshot.success", panoramaTakenText);
                client.gui.getChat().addMessage(message);
                client.getNarrator().sayNow(message);
            }
        });

        // endregion

        // region Structure Music

        PayloadTypeRegistry.playS2C().register(StructureChangedPayload.TYPE, StructureChangedPayload.CODEC);

        ServerWorldEvents.UNLOAD.register((server, level) -> {
            if (level.isClientSide() && !StructureMusic.playerStructures.isEmpty()) StructureMusic.playerStructures.clear();
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> StructureMusic.playerStructures.remove(handler.getPlayer()));

        ServerTickEvents.START_WORLD_TICK.register((serverLevel) -> {

            if (StructureMusic.ticks > 0) {
                StructureMusic.ticks -= 1;
                return;
            }

            java.util.List<ServerPlayer> list = serverLevel.getPlayers(LivingEntity::isAlive);
            for (ServerPlayer serverPlayer : list) StructureMusic.checkAllStructures(serverLevel, serverPlayer);

            StructureMusic.ticks = 250;
        });

        // endregion
    }
}