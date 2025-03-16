package net.sydokiddo.chrysalis.common;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenshotEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.ChrysalisItems;
import net.sydokiddo.chrysalis.common.misc.ChrysalisSoundEvents;
import net.sydokiddo.chrysalis.util.helpers.CompatibilityHelper;
import net.sydokiddo.chrysalis.util.technical.ClipboardImage;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import net.sydokiddo.chrysalis.util.technical.splash_texts.SplashTextLoader;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

@SuppressWarnings("unused")
public class ChrysalisClientEvents {

    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class GameEventBus {

        @SubscribeEvent
        private static void onClientTick(ClientTickEvent.Post event) {

            while (ChrysalisRegistry.PANORAMIC_SCREENSHOT_KEY.consumeClick()) {

                Minecraft minecraft = Minecraft.getInstance();
                if (!CompatibilityHelper.isModLoaded("essential")) playScreenshotSound(minecraft);

                Component message = Component.translatable("screenshot.success",
                Component.literal("panorama_0.png – panorama_5.png").withStyle(ChatFormatting.UNDERLINE).withStyle((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE,
                new File(FMLLoader.getGamePath().toString() + "/screenshots/").getAbsolutePath()))));

                minecraft.grabPanoramixScreenshot(new File(FMLLoader.getGamePath().toString()), 1024, 1024);
                minecraft.gui.getChat().addMessage(message);
                minecraft.getNarrator().sayNow(message);
            }
        }

        @SuppressWarnings("all")
        @SubscribeEvent
        private static void onScreenShot(ScreenshotEvent event) {

            if (event.getScreenshotFile().getName().contains("panorama") || CompatibilityHelper.isModLoaded("essential") || CompatibilityHelper.isModLoaded("optifine") || CompatibilityHelper.isModLoaded("optifabric") || Minecraft.ON_OSX) return;
            Minecraft minecraft = Minecraft.getInstance();
            playScreenshotSound(minecraft);

            try {

                Path path = minecraft.gameDirectory.toPath().resolve(Screenshot.SCREENSHOT_DIR);
                Optional<Path> lastFilePath = Files.list(path).filter(directory -> !Files.isDirectory(directory)).max(Comparator.comparingLong(directory -> directory.toFile().lastModified()));

                if (lastFilePath.isEmpty()) return;
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new ClipboardImage(new ImageIcon(lastFilePath.get().toString()).getImage()), null);

                Component message = Component.translatable("gui.chrysalis.screenshot_copied");
                minecraft.gui.getChat().addMessage(message);
                minecraft.getNarrator().sayNow(message);

            } catch (Exception exception) {
                Chrysalis.LOGGER.warn("Failed to copy screenshot to clipboard", exception);
            }
        }

        private static void playScreenshotSound(Minecraft minecraft) {
            if (CConfigOptions.SCREENSHOT_SOUND.get()) minecraft.getSoundManager().play(SimpleSoundInstance.forUI(ChrysalisSoundEvents.SCREENSHOT_SUCCESS.get(), 1.0F));
        }
    }

    @EventBusSubscriber(modid = Chrysalis.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEventBus {

        @SubscribeEvent
        private static void registerClientResources(AddClientReloadListenersEvent event) {
            event.addListener(Chrysalis.resourceLocationId("splashes"), SplashTextLoader.INSTANCE);
        }

        @SubscribeEvent
        private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(ChrysalisRegistry.PANORAMIC_SCREENSHOT_KEY);
        }

        @SubscribeEvent
        public static void addItemsToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions()) {
                event.insertAfter(Items.DEBUG_STICK.getDefaultInstance(), ChrysalisItems.HEAL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.HEAL.toStack(), ChrysalisItems.FILL_HUNGER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.FILL_HUNGER.toStack(), ChrysalisItems.FILL_OXYGEN.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.FILL_OXYGEN.toStack(), ChrysalisItems.GIVE_RESISTANCE.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.GIVE_RESISTANCE.toStack(), ChrysalisItems.CLEAR_EFFECTS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.CLEAR_EFFECTS.toStack(), ChrysalisItems.TELEPORT_TO_SPAWNPOINT.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.TELEPORT_TO_SPAWNPOINT.toStack(), ChrysalisItems.TELEPORT_WAND.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.TELEPORT_WAND.toStack(), ChrysalisItems.KILL_WAND.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.KILL_WAND.toStack(), ChrysalisItems.AGGRO_WAND.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.AGGRO_WAND.toStack(), ChrysalisItems.TAME_MOB.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(ChrysalisItems.TAME_MOB.toStack(), ChrysalisItems.RIDE_MOB.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(Items.ENDER_DRAGON_SPAWN_EGG.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(Items.WITHER_SPAWN_EGG.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }
}