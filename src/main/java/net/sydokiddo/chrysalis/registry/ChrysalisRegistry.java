package net.sydokiddo.chrysalis.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.client.ChrysalisClient;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDataComponents;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.sydokiddo.chrysalis.util.helpers.RegistryHelper;
import net.sydokiddo.chrysalis.util.sounds.codecs.BlockSoundData;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakeResetPayload;
import net.sydokiddo.chrysalis.util.technical.commands.*;
import net.sydokiddo.chrysalis.util.entities.ChrysalisMemoryModules;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.util.sounds.music.*;
import net.sydokiddo.chrysalis.registry.entities.registry.ChrysalisEntities;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.misc.*;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;
import java.util.*;

public class ChrysalisRegistry {

    // region Entity Data

    public static final EntityDataAccessor<Integer>
        ITEM_GLOW_COLOR = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.INT)
    ;

    public static final EntityDataAccessor<Optional<UUID>>
        ENCOUNTERED_MOB_UUID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UUID)
    ;

    // endregion

    // region Custom Music

    public static Map<String, StructureMusicSound> registeredStructures = new HashMap<>();
    public record StructureMusicSound(Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {}

    // endregion

    // region Data Driven Features

    public static final ResourceKey<Registry<BlockPropertyData>> BLOCK_PROPERTY_DATA = RegistryHelper.registerBlockDataType("properties");
    public static final ResourceKey<Registry<BlockSoundData>> BLOCK_SOUND_DATA = RegistryHelper.registerBlockDataType("sound_group");

    // endregion

    public static void registerAll(IEventBus modBus) {

        modBus.addListener(ChrysalisRegistry::onServerAboutToStart);
        modBus.addListener(ChrysalisRegistry::onServerStarted);
        modBus.addListener(ChrysalisRegistry::onServerStopping);
        modBus.addListener(ChrysalisRegistry::onPlayerDisconnect);
        modBus.addListener(ChrysalisRegistry::datapackRegistry);

        // region Base Registries

        ChrysalisDebugItems.registerDebugItems();
        ChrysalisDataComponents.registerDataComponents();
        ChrysalisCreativeModeTabs.registerCreativeTabs();
        ChrysalisSoundEvents.registerSounds();
        ChrysalisSoundEvents.registerStructureMusic();
        ChrysalisEntities.registerEntities();
        ChrysalisMemoryModules.MEMORY_MODULES.register();
        ChrysalisDamageSources.registerDamageSources();
        ChrysalisAttributes.registerAttributes();
        ChrysalisEffects.registerStatusEffects();
        ChrysalisGameRules.registerGameRules();
        ChrysalisCriteriaTriggers.registerCriteriaTriggers();

        // endregion

        // region Commands

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> CameraShakeCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> ClearSpawnpointCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> CooldownCommand.register(commandDispatcher, commandBuildContext));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> CoordinatesCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> DisenchantCommand.register(commandDispatcher, commandBuildContext));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> DurabilityCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> ExplosionCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> FoodCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> GameEventCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> HatCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> HealCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> MusicCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> OxygenCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> ShowcaseCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> SurfaceCommand.register(commandDispatcher));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> TrimCommand.register(commandDispatcher, commandBuildContext));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> VelocityCommand.register(commandDispatcher));

        // endregion

        // region Payloads

        PayloadTypeRegistry.playS2C().register(CameraShakePayload.TYPE, CameraShakePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraShakeResetPayload.TYPE, CameraShakeResetPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(QueuedMusicPayload.TYPE, QueuedMusicPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StructureChangedPayload.TYPE, StructureChangedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ClearMusicPayload.TYPE, ClearMusicPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ResetMusicFadePayload.TYPE, ResetMusicFadePayload.CODEC);

        // endregion
    }

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        ChrysalisMod.registryAccess = event.getServer().registryAccess();
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {

        if (StructureMusic.ticks > 0) {
            StructureMusic.ticks -= 1;
            return;
        }

        List<ServerPlayer> list = event.getServer().getPlayerList().getPlayers();

        for (ServerPlayer serverPlayer : list) {
            if (!serverPlayer.isAlive()) return;
            StructureMusic.checkAllStructures(serverPlayer.serverLevel(), serverPlayer);
        }

        StructureMusic.ticks = 250;
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        ChrysalisClient.clearMusicOnClient(false);
        if (!StructureMusic.playerStructures.isEmpty()) StructureMusic.playerStructures.clear();
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            EventHelper.clearMusicOnServer(serverPlayer, false);
            StructureMusic.playerStructures.remove(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void datapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(BLOCK_PROPERTY_DATA, BlockPropertyData.CODEC);
        event.dataPackRegistry(BLOCK_SOUND_DATA, BlockSoundData.CODEC);
    }
}