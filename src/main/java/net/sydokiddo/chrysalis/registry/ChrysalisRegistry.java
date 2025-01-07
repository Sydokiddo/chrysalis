package net.sydokiddo.chrysalis.registry;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.client.ChrysalisClient;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakeResetPayload;
import net.sydokiddo.chrysalis.misc.util.commands.*;
import net.sydokiddo.chrysalis.misc.util.entities.ChrysalisMemoryModules;
import net.sydokiddo.chrysalis.misc.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.misc.util.music.ClearMusicPayload;
import net.sydokiddo.chrysalis.misc.util.music.QueuedMusicPayload;
import net.sydokiddo.chrysalis.misc.util.music.StructureChangedPayload;
import net.sydokiddo.chrysalis.misc.util.music.StructureMusic;
import net.sydokiddo.chrysalis.registry.entities.registry.ChrysalisEntities;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.misc.*;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    public static GameRules.Key<GameRules.BooleanValue> RULE_BEDS_EXPLODE =
        GameRuleRegistry.register(
        "bedsExplode",
        GameRules.Category.DROPS,
        GameRuleFactory.createBooleanRule(true)
    );
    public static GameRules.Key<GameRules.BooleanValue> RULE_RESPAWN_ANCHORS_EXPLODE =
        GameRuleRegistry.register(
        "respawnAnchorsExplode",
        GameRules.Category.DROPS,
        GameRuleFactory.createBooleanRule(true)
    );

    // endregion

    // region Entity Data

    public static final EntityDataAccessor<Integer>
        ITEM_GLOW_COLOR = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.INT)
    ;

    public static final EntityDataAccessor<Optional<UUID>>
        ENCOUNTERED_MOB_UUID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UUID)
    ;

    // endregion

    // region Colors and Fonts

    public static final Color
        CHRYSALIS_COLOR = Color.decode("#A27FFF"),
        WAXED_COLOR = Color.decode("#FABF29")
    ;

    @SuppressWarnings("unused")
    public static final MutableComponent
        CHRYSALIS_ICON = Component.translatable("gui.icon.chrysalis.mod_icon"),
        GEAR_ICON = Component.translatable("gui.icon.chrysalis.gear"),
        WARNING_ICON = Component.translatable("gui.icon.chrysalis.warning"),
        QUESTION_MARK_ICON = Component.translatable("gui.icon.chrysalis.question_mark"),
        TOOLTIP_ICON = Component.translatable("gui.icon.chrysalis.tooltip"),
        POTION_ICON = Component.translatable("gui.icon.chrysalis.potion"),
        ARMOR_ICON = Component.translatable("gui.icon.chrysalis.armor"),
        RED_HEART_ICON = Component.translatable("gui.icon.chrysalis.red_heart"),
        ORANGE_HEART_ICON = Component.translatable("gui.icon.chrysalis.orange_heart"),
        YELLOW_HEART_ICON = Component.translatable("gui.icon.chrysalis.yellow_heart"),
        GREEN_HEART_ICON = Component.translatable("gui.icon.chrysalis.green_heart"),
        BLUE_HEART_ICON = Component.translatable("gui.icon.chrysalis.blue_heart"),
        PURPLE_HEART_ICON = Component.translatable("gui.icon.chrysalis.purple_heart"),
        PINK_HEART_ICON = Component.translatable("gui.icon.chrysalis.pink_heart"),
        WHITE_HEART_ICON = Component.translatable("gui.icon.chrysalis.white_heart"),
        GRAY_HEART_ICON = Component.translatable("gui.icon.chrysalis.gray_heart"),
        BLACK_HEART_ICON = Component.translatable("gui.icon.chrysalis.black_heart"),
        BROWN_HEART_ICON = Component.translatable("gui.icon.chrysalis.brown_heart"),
        SNOUT_ICON = Component.translatable("gui.icon.chrysalis.snout"),
        WAXED_ICON = Component.translatable("gui.icon.chrysalis.waxed")
    ;

    @SuppressWarnings("unused")
    public static final ResourceLocation
        FIVE_FONT = Chrysalis.id("five"),
        FIVE_ALT_FONT = Chrysalis.id("five_alt")
    ;

    public static final String TOOLTIP_ICONS_NAME = "tooltip_icons";

    // endregion

    // region Structure Music

    public static Map<String, StructureMusicSound> registeredStructures = new HashMap<>();
    public record StructureMusicSound(Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {}

    // endregion

    public static void registerAll() {

        // region Base Registries

        ChrysalisDebugItems.registerDebugItems();
        ChrysalisCreativeModeTabs.registerCreativeTabs();
        ChrysalisSoundEvents.registerSounds();
        ChrysalisSoundEvents.registerStructureMusic();
        ChrysalisDamageSources.registerDamageSources();
        ChrysalisCriteriaTriggers.registerCriteriaTriggers();
        ChrysalisEntities.registerEntities();
        ChrysalisMemoryModules.MEMORY_MODULES.register();
        ChrysalisAttributes.registerAttributes();
        ChrysalisEffects.registerStatusEffects();

        PayloadTypeRegistry.playS2C().register(CameraShakePayload.TYPE, CameraShakePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraShakeResetPayload.TYPE, CameraShakeResetPayload.CODEC);

        // endregion

        // region Commands

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> HealCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> FoodCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> OxygenCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> CooldownCommand.register(commandDispatcher, commandBuildContext));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> HatCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> ShowcaseCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> SurfaceCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> ClearSpawnpointCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> DisenchantCommand.register(commandDispatcher, commandBuildContext));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> DurabilityCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> TrimCommand.register(commandDispatcher, commandBuildContext));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> ExplosionCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> MusicCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> GameEventCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> VelocityCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> CameraShakeCommand.register(commandDispatcher));

        // endregion

        // region Custom Music

        PayloadTypeRegistry.playS2C().register(QueuedMusicPayload.TYPE, QueuedMusicPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StructureChangedPayload.TYPE, StructureChangedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ClearMusicPayload.TYPE, ClearMusicPayload.CODEC);

        ServerWorldEvents.UNLOAD.register((server, level) -> {
            if (!level.isClientSide()) return;
            ChrysalisClient.clearMusicOnClient(false);
            if (!StructureMusic.playerStructures.isEmpty()) StructureMusic.playerStructures.clear();
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            EventHelper.clearMusicOnServer(handler.getPlayer(), false);
            StructureMusic.playerStructures.remove(handler.getPlayer());
        });

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