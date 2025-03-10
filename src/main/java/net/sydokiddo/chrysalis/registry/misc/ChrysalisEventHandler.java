package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenshotEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.registry.ChrysalisClientRegistry;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.sydokiddo.chrysalis.util.helpers.CompatibilityHelper;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.util.sounds.codecs.BlockSoundData;
import net.sydokiddo.chrysalis.util.sounds.music.*;
import net.sydokiddo.chrysalis.util.technical.ClipboardImage;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakeResetPayload;
import net.sydokiddo.chrysalis.util.technical.commands.*;
import net.sydokiddo.chrysalis.util.technical.splash_texts.SplashTextLoader;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

@SuppressWarnings("unused")
public class ChrysalisEventHandler implements IModBusEvent {

    @SubscribeEvent
    private void onServerAboutToStart(ServerAboutToStartEvent event) {
        ChrysalisMod.registryAccess = event.getServer().registryAccess();
    }

    @SubscribeEvent
    private void onServerStarted(ServerStartedEvent event) {

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
    private void onServerStopping(ServerStoppingEvent event) {
        ChrysalisClientRegistry.clearMusicOnClient(false);
        if (!StructureMusic.playerStructures.isEmpty()) StructureMusic.playerStructures.clear();
    }

    @SubscribeEvent
    private void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            EventHelper.clearMusicOnServer(serverPlayer, false);
            StructureMusic.playerStructures.remove(serverPlayer);
        }
    }

    @SubscribeEvent
    private void datapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ChrysalisRegistry.BLOCK_PROPERTY_DATA, BlockPropertyData.CODEC);
        event.dataPackRegistry(ChrysalisRegistry.BLOCK_SOUND_DATA, BlockSoundData.CODEC);
    }

    @SubscribeEvent
    private void commandRegistry(RegisterCommandsEvent event) {
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

    @SubscribeEvent
    private void payloadRegistry(RegisterPayloadHandlersEvent event) {

        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
            CameraShakePayload.TYPE,
            CameraShakePayload.CODEC,
            CameraShakePayload::handleDataOnClient
        );

        registrar.playToClient(
            CameraShakeResetPayload.TYPE,
            CameraShakeResetPayload.CODEC,
            CameraShakeResetPayload::handleDataOnClient
        );

        registrar.playToClient(
            QueuedMusicPayload.TYPE,
            QueuedMusicPayload.CODEC,
            QueuedMusicPayload::handleDataOnClient
        );

        registrar.playToClient(
            StructureChangedPayload.TYPE,
            StructureChangedPayload.CODEC,
            StructureChangedPayload::handleDataOnClient
        );

        registrar.playToClient(
            ClearMusicPayload.TYPE,
            ClearMusicPayload.CODEC,
            ClearMusicPayload::handleDataOnClient
        );

        registrar.playToClient(
            ResetMusicFadePayload.TYPE,
            ResetMusicFadePayload.CODEC,
            ResetMusicFadePayload::handleDataOnClient
        );
    }

    @SubscribeEvent
    private void mobGriefingEventRegistry(EntityMobGriefingEvent event) {
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

    @OnlyIn(Dist.CLIENT)
    public static class ClientEvents implements IModBusEvent {

        @SubscribeEvent
        private void registerClientResources(AddClientReloadListenersEvent event) {
            event.addListener(ChrysalisMod.id("splashes"), SplashTextLoader.INSTANCE);
        }

        @SubscribeEvent
        private void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(ChrysalisClientRegistry.PANORAMIC_SCREENSHOT_KEY);
        }

        @SubscribeEvent
        private void onClientTick(ClientTickEvent.Post event) {

            while (ChrysalisClientRegistry.PANORAMIC_SCREENSHOT_KEY.consumeClick()) {

                Minecraft minecraft = Minecraft.getInstance();
                minecraft.grabPanoramixScreenshot(new File(FMLLoader.getGamePath().toString()), 1024, 1024);

                Component panoramaTakenText = Component.literal("panorama_0.png – panorama_5.png").withStyle(ChatFormatting.UNDERLINE)
                        .withStyle((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, new File(FMLLoader.getGamePath().toString() + "/screenshots/").getAbsolutePath())));

                Component message = Component.translatable("screenshot.success", panoramaTakenText);
                minecraft.gui.getChat().addMessage(message);
                minecraft.getNarrator().sayNow(message);
            }
        }

        @SubscribeEvent
        private void onScreenShot(ScreenshotEvent event) {

            if (CompatibilityHelper.isModLoaded("essential") || CompatibilityHelper.isModLoaded("optifine") || CompatibilityHelper.isModLoaded("optifabric") || Minecraft.ON_OSX) return;
            Minecraft minecraft = Minecraft.getInstance();

            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(ChrysalisSoundEvents.SCREENSHOT_SUCCESS, 1.0F));

            Image lastScreenShot = new ImageIcon(event.getScreenshotFile().toString()).getImage();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new ClipboardImage(lastScreenShot), null);

            Component message = Component.translatable("gui.chrysalis.screenshot_copied");
            minecraft.gui.getChat().addMessage(message);
            minecraft.getNarrator().sayNow(message);
        }
    }
}