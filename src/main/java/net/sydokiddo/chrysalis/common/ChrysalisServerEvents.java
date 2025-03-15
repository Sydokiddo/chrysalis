package net.sydokiddo.chrysalis.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.misc.ChrysalisGameRules;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.util.sounds.codecs.BlockSoundData;
import net.sydokiddo.chrysalis.util.sounds.music.*;
import net.sydokiddo.chrysalis.util.sounds.music.payloads.ClearMusicPayload;
import net.sydokiddo.chrysalis.util.sounds.music.payloads.QueuedMusicPayload;
import net.sydokiddo.chrysalis.util.sounds.music.payloads.ResetMusicFadePayload;
import net.sydokiddo.chrysalis.util.sounds.music.payloads.StructureChangedPayload;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakeResetPayload;
import net.sydokiddo.chrysalis.util.technical.commands.*;
import java.util.List;

@SuppressWarnings("unused")
public class ChrysalisServerEvents {

    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class GameEventBus {

        @SubscribeEvent
        private static void onServerAboutToStart(ServerAboutToStartEvent event) {
            Chrysalis.registryAccess = event.getServer().registryAccess();
        }

        @SubscribeEvent
        private static void onServerStarted(ServerStartedEvent event) {

            if (MusicTracker.ticks > 0) {
                MusicTracker.ticks -= 1;
                return;
            }

            List<ServerPlayer> list = event.getServer().getPlayerList().getPlayers();

            for (ServerPlayer serverPlayer : list) {
                if (!serverPlayer.isAlive()) return;
                MusicTracker.checkAllStructures(serverPlayer.serverLevel(), serverPlayer);
            }

            MusicTracker.ticks = 250;
        }

        @SubscribeEvent
        private static void onServerStopping(ServerStoppingEvent event) {
            MusicTracker.clearMusicOnClient(false);
            if (!MusicTracker.playerStructures.isEmpty()) MusicTracker.playerStructures.clear();
        }

        @SubscribeEvent
        private static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                EventHelper.clearMusicOnServer(serverPlayer, false);
                MusicTracker.playerStructures.remove(serverPlayer);
            }
        }

        @SubscribeEvent
        private static void mobGriefingEvents(EntityMobGriefingEvent event) {
            if (event.getEntity() instanceof Allay allay && allay.getServer() != null) event.setCanGrief(allay.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Evoker evoker && evoker.getServer() != null) event.setCanGrief(evoker.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Fox fox && fox.getServer() != null) event.setCanGrief(fox.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Piglin piglin && piglin.getServer() != null) event.setCanGrief(piglin.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Rabbit rabbit && rabbit.getServer() != null) event.setCanGrief(rabbit.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Sheep sheep && sheep.getServer() != null) event.setCanGrief(sheep.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof SnowGolem snowGolem && snowGolem.getServer() != null) event.setCanGrief(snowGolem.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof Villager villager && villager.getServer() != null) event.setCanGrief(villager.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS));
            if (event.getEntity() instanceof EnderDragon enderDragon && enderDragon.getServer() != null) event.setCanGrief(enderDragon.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_DRAGON_GRIEFING));
            if (event.getEntity() instanceof WitherBoss wither && wither.getServer() != null) event.setCanGrief(wither.getServer().getGameRules().getBoolean(ChrysalisGameRules.RULE_WITHER_GRIEFING));
        }

        @SubscribeEvent
        private static void commandRegistry(RegisterCommandsEvent event) {
            CameraShakeCommand.register(event.getDispatcher());
            ClearSpawnpointCommand.register(event.getDispatcher());
            CooldownCommand.register(event.getDispatcher(), event.getBuildContext());
            CoordinatesCommand.register(event.getDispatcher());
            DisenchantCommand.register(event.getDispatcher(), event.getBuildContext());
            DurabilityCommand.register(event.getDispatcher());
            ExplosionCommand.register(event.getDispatcher());
            FoodCommand.register(event.getDispatcher());
            GameEventCommand.register(event.getDispatcher());
            HatCommand.register(event.getDispatcher());
            HealCommand.register(event.getDispatcher());
            MusicCommand.register(event.getDispatcher());
            OxygenCommand.register(event.getDispatcher());
            ShowcaseCommand.register(event.getDispatcher());
            SurfaceCommand.register(event.getDispatcher());
            TrimCommand.register(event.getDispatcher(), event.getBuildContext());
            VelocityCommand.register(event.getDispatcher());
        }
    }

    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEventBus {

        @SubscribeEvent
        private static void datapackRegistry(DataPackRegistryEvent.NewRegistry event) {
            event.dataPackRegistry(ChrysalisRegistry.BLOCK_PROPERTY_DATA, BlockPropertyData.CODEC);
            event.dataPackRegistry(ChrysalisRegistry.BLOCK_SOUND_DATA, BlockSoundData.CODEC);
        }

        @SubscribeEvent
        private static void payloadRegistry(RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playToClient(CameraShakePayload.TYPE, CameraShakePayload.CODEC, CameraShakePayload::handleDataOnClient);
            registrar.playToClient(CameraShakeResetPayload.TYPE, CameraShakeResetPayload.CODEC, CameraShakeResetPayload::handleDataOnClient);
            registrar.playToClient(QueuedMusicPayload.TYPE, QueuedMusicPayload.CODEC, QueuedMusicPayload::handleDataOnClient);
            registrar.playToClient(StructureChangedPayload.TYPE, StructureChangedPayload.CODEC, StructureChangedPayload::handleDataOnClient);
            registrar.playToClient(ClearMusicPayload.TYPE, ClearMusicPayload.CODEC, ClearMusicPayload::handleDataOnClient);
            registrar.playToClient(ResetMusicFadePayload.TYPE, ResetMusicFadePayload.CODEC, ResetMusicFadePayload::handleDataOnClient);
        }
    }
}