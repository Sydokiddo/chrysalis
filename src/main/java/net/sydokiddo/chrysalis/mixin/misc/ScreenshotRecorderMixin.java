package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.ClipboardImage;
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

@Environment(EnvType.CLIENT)
@Mixin(Screenshot.class)
public class ScreenshotRecorderMixin {

    /**
     * Copies screenshots to the user's clipboard upon taking a screenshot.
     **/

    @SuppressWarnings("all")
    @Inject(method = "method_1661", at = @At("TAIL"))
    private static void chrysalis$copyScreenshotToClipboard(NativeImage image, File file, Consumer<Component> messageReceiver, CallbackInfo info) {

        if (FabricLoader.getInstance().isModLoaded("essential") || FabricLoader.getInstance().isModLoaded("optifine") || FabricLoader.getInstance().isModLoaded("optifabric") || Minecraft.ON_OSX) return;
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
            Chrysalis.LOGGER.warn("Failed to copy screenshot to clipboard", exception);
            exception.printStackTrace();
        }
    }
}