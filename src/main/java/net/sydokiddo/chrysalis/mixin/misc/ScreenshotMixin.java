package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.main.Main;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLLoader;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.util.technical.ClipboardImage;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import javax.swing.*;
import com.mojang.blaze3d.platform.NativeImage;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(Screenshot.class)
public class ScreenshotMixin {

    /**
     * Copies screenshots to the user's clipboard upon taking a screenshot.
     **/

    @SuppressWarnings("all")
    @Inject(method = "method_1661", at = @At("TAIL"))
    private static void chrysalis$copyScreenshotToClipboard(NativeImage image, File file, Consumer<Component> messageReceiver, CallbackInfo info) {
        
        if (FMLLoader.getLoadingModList().getModFileById("essential") != null || FMLLoader.getLoadingModList().getModFileById("optifine") != null || FMLLoader.getLoadingModList().getModFileById("optifabric") != null || Minecraft.ON_OSX) return;
        Minecraft minecraft = Minecraft.getInstance();

        try {
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(ChrysalisSoundEvents.SCREENSHOT_SUCCESS, 1.0F));

            Path path = minecraft.gameDirectory.toPath().resolve(Screenshot.SCREENSHOT_DIR);
            Optional<Path> lastFilePath = Files.list(path).filter(directory -> !Files.isDirectory(directory)).max(Comparator.comparingLong(directory -> directory.toFile().lastModified()));

            if (lastFilePath.isEmpty()) return;

            Image lastScreenShot = new ImageIcon(lastFilePath.get().toString()).getImage();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new ClipboardImage(lastScreenShot), null);

            Component message = Component.translatable("gui.chrysalis.screenshot_copied");
            minecraft.gui.getChat().addMessage(message);
            minecraft.getNarrator().sayNow(message);

        } catch (Exception exception) {
            ChrysalisMod.LOGGER.warn("Failed to copy screenshot to clipboard", exception);
            exception.printStackTrace();
        }
    }

    @Mixin(Main.class)
    public static class MainMixin {

        /**
         * Additional mixin code for the screenshot to clipboard feature.
         **/

        @Inject(method = "main", at = @At("HEAD"), remap = false)
        private static void chrysalis$setScreenshotProperty(CallbackInfo info) {
            System.setProperty("java.awt.headless", "false");
        }
    }
}