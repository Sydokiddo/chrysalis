package net.junebug.chrysalis.common;

import net.junebug.chrysalis.common.blocks.CBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.client.particles.types.*;
import net.junebug.chrysalis.common.items.CItems;
import net.junebug.chrysalis.common.misc.CParticles;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.util.helpers.CompatibilityHelper;
import net.junebug.chrysalis.util.helpers.EventHelper;
import net.junebug.chrysalis.util.sounds.music.payloads.ClearMusicPayload;
import net.junebug.chrysalis.util.sounds.music.payloads.QueuedMusicPayload;
import net.junebug.chrysalis.util.sounds.music.payloads.ResetMusicFadePayload;
import net.junebug.chrysalis.util.sounds.music.payloads.StructureChangedPayload;
import net.junebug.chrysalis.util.technical.camera.CameraShakePayload;
import net.junebug.chrysalis.util.technical.camera.CameraShakeResetPayload;
import net.junebug.chrysalis.util.technical.config.CConfigOptions;
import net.junebug.chrysalis.util.technical.splash_texts.SplashTextLoader;
import java.io.File;

@SuppressWarnings("unused")
public class CClientEvents {

    /**
     * Executes mod events on the client.
     **/

    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class GameEventBus {

        @SubscribeEvent
        private static void onClientTick(ClientTickEvent.Post event) {

            while (CRegistry.ClientRegistry.PANORAMIC_SCREENSHOT_KEY.consumeClick()) {

                Minecraft minecraft = Minecraft.getInstance();
                if (canPlayScreenshotEvents(null, false)) playScreenshotSound(minecraft);

                Component message = Component.translatable("screenshot.success",
                Component.literal("panorama_0.png – panorama_5.png").withStyle(ChatFormatting.UNDERLINE).withStyle((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE,
                new File(FMLLoader.getGamePath().toString() + "/screenshots/").getAbsolutePath()))));

                minecraft.grabPanoramixScreenshot(new File(FMLLoader.getGamePath().toString()), 1024, 1024);
                minecraft.gui.getChat().addMessage(message);
                minecraft.getNarrator().sayNow(message);
            }
        }

        @SubscribeEvent
        private static void onScreenshot(ScreenshotEvent event) {
            if (canPlayScreenshotEvents(event, true)) playScreenshotSound(Minecraft.getInstance());
        }

        public static boolean canPlayScreenshotEvents(ScreenshotEvent event, boolean checkForPanorama) {
            if (checkForPanorama && event != null && event.getScreenshotFile().getName().contains("panorama")) return false;
            return !CompatibilityHelper.isModLoaded("essential") && !CompatibilityHelper.isModLoaded("optifine") && !CompatibilityHelper.isModLoaded("optifabric") && !Minecraft.ON_OSX;
        }

        private static void playScreenshotSound(Minecraft minecraft) {
            if (CConfigOptions.SCREENSHOT_SOUND.get()) EventHelper.playUIClickSound(minecraft, CSoundEvents.SCREENSHOT_SUCCESS);
        }
    }

    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEventBus {

        @SubscribeEvent
        private static void clientResourceRegistry(AddClientReloadListenersEvent event) {
            event.addListener(Chrysalis.resourceLocationId("splashes"), SplashTextLoader.INSTANCE);
        }

        @SubscribeEvent
        private static void keyMappingRegistry(RegisterKeyMappingsEvent event) {
            event.register(CRegistry.ClientRegistry.PANORAMIC_SCREENSHOT_KEY);
        }

        @SubscribeEvent
        private static void clientPayloadRegistry(RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playToClient(CameraShakePayload.TYPE, CameraShakePayload.CODEC, CameraShakePayload::handleDataOnClient);
            registrar.playToClient(CameraShakeResetPayload.TYPE, CameraShakeResetPayload.CODEC, CameraShakeResetPayload::handleDataOnClient);
            registrar.playToClient(QueuedMusicPayload.TYPE, QueuedMusicPayload.CODEC, QueuedMusicPayload::handleDataOnClient);
            registrar.playToClient(StructureChangedPayload.TYPE, StructureChangedPayload.CODEC, StructureChangedPayload::handleDataOnClient);
            registrar.playToClient(ClearMusicPayload.TYPE, ClearMusicPayload.CODEC, ClearMusicPayload::handleDataOnClient);
            registrar.playToClient(ResetMusicFadePayload.TYPE, ResetMusicFadePayload.CODEC, ResetMusicFadePayload::handleDataOnClient);
        }

        @SubscribeEvent
        private static void particleProviderRegistry(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(CParticles.MEMORY_FLAME.get(), FlameParticle.Provider::new);
            event.registerSpriteSet(CParticles.LAVA_BUBBLE.get(), CustomBubbleParticle.LavaProvider::new);
            event.registerSpriteSet(CParticles.RADIANCE.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.ARTHROPOD_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.BLIND_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.CREEPER_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.ENDER_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.RESIN_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.SKELETAL_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.ZOMBIE_SIGHT.get(), SpellParticle.Provider::new);
            event.registerSpriteSet(CParticles.COLORED_DUST_PLUME.get(), ColoredDustPlumeParticle.Provider::new);
            event.registerSpriteSet(CParticles.COLORED_DIRECTIONAL_DUST.get(), ColoredDirectionalDustParticle.Provider::new);
            event.registerSpriteSet(CParticles.COLORED_PORTAL.get(), ColoredPortalParticle.Provider::new);
            event.registerSpriteSet(CParticles.DUST_EXPLOSION.get(), DustExplosionParticle.Provider::new);
            event.registerSpriteSet(CParticles.ROTATING_DUST.get(), RotatingDustParticle.Provider::new);
            event.registerSpriteSet(CParticles.SPARKLE.get(), SparkleParticle.Provider::new);
            event.registerSpriteSet(CParticles.SPARK.get(), SparkParticle.Provider::new);
            event.registerSpriteSet(CParticles.SMALL_PULSATION.get(), PulsationParticle.SmallProvider::new);
            event.registerSpriteSet(CParticles.LARGE_PULSATION.get(), PulsationParticle.LargeProvider::new);
            event.registerSpriteSet(CParticles.MUSIC_NOTE.get(), MusicNoteParticle.Provider::new);
            event.registerSpriteSet(CParticles.DUST_CLOUD.get(), DustCloudParticle.Provider::new);
        }

        @SubscribeEvent
        private static void addItemsToCreativeTabs(BuildCreativeModeTabContentsEvent event) {

            if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
                event.insertAfter(Items.ENDER_EYE.getDefaultInstance(), CItems.NETHER_PORTAL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.NETHER_PORTAL.toStack(), CItems.END_PORTAL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.END_PORTAL.toStack(), CItems.END_GATEWAY.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) event.insertBefore(Items.TRIAL_KEY.getDefaultInstance(), CItems.KEY.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
                event.insertAfter(Items.CREAKING_HEART.getDefaultInstance(), CItems.COPYING_SPAWN_EGG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.GHAST_SPAWN_EGG.getDefaultInstance(), CItems.GIANT_SPAWN_EGG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.HUSK_SPAWN_EGG.getDefaultInstance(), CItems.ILLUSIONER_SPAWN_EGG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions()) {
                event.insertAfter(Items.STRUCTURE_BLOCK.getDefaultInstance(), CBlocks.PLACEHOLDER_BLOCK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(Items.DEBUG_STICK.getDefaultInstance(), CItems.HEAL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.HEAL.toStack(), CItems.FILL_HUNGER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.FILL_HUNGER.toStack(), CItems.FILL_OXYGEN.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.FILL_OXYGEN.toStack(), CItems.GIVE_RESISTANCE.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.GIVE_RESISTANCE.toStack(), CItems.CLEAR_EFFECTS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.CLEAR_EFFECTS.toStack(), CItems.TELEPORT_TO_SPAWNPOINT.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.TELEPORT_TO_SPAWNPOINT.toStack(), CItems.TELEPORT_TO_OVERWORLD.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.TELEPORT_TO_OVERWORLD.toStack(), CItems.TELEPORT_TO_NETHER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.TELEPORT_TO_NETHER.toStack(), CItems.TELEPORT_TO_END.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.TELEPORT_TO_END.toStack(), CItems.TELEPORT_WAND.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.TELEPORT_WAND.toStack(), CItems.KILL_WAND.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.KILL_WAND.toStack(), CItems.AGGRO_WAND.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.AGGRO_WAND.toStack(), CItems.TAME_MOB.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.TAME_MOB.toStack(), CItems.RIDE_MOB.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.RIDE_MOB.toStack(), CItems.DRAIN_FLUIDS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.DRAIN_FLUIDS.toStack(), CItems.COPYING_SPAWN_EGG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.COPYING_SPAWN_EGG.toStack(), CItems.ADMIN_KEY.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.ADMIN_KEY.toStack(), CItems.MUSIC_EVENT_TEST.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.MUSIC_EVENT_TEST.toStack(), CItems.CAMERA_SHAKE_EVENT_TEST.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.CAMERA_SHAKE_EVENT_TEST.toStack(), CItems.EARTHQUAKE_EVENT_TEST.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.EARTHQUAKE_EVENT_TEST.toStack(), CItems.ENTITY_SPAWNER_EVENT_TEST.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(CItems.ENTITY_SPAWNER_EVENT_TEST.toStack(), CItems.ENCOUNTER_SPAWNER_EVENT_TEST.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                if (Chrysalis.IS_DEBUG && Chrysalis.registerTestItems) event.insertAfter(CItems.ENCOUNTER_SPAWNER_EVENT_TEST.toStack(), CItems.TEST_RIGHT_CLICK_ITEM.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(Items.ENDER_DRAGON_SPAWN_EGG.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(Items.WITHER_SPAWN_EGG.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }
}