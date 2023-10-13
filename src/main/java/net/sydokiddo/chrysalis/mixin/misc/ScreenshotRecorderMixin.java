package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.Component;
import net.sydokiddo.chrysalis.misc.util.ClipboardImage;
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
    private static void chrysalis$copyScreenshotToClipboard(NativeImage image, File file, Consumer<Component> messageReceiver, CallbackInfo ci) {

        Minecraft mc = Minecraft.getInstance();

        try {
            Path path = mc.gameDirectory.toPath().resolve("screenshots");
            Optional<Path> lastFilePath = Files.list(path).filter(f -> !Files.isDirectory(f)).max(Comparator.comparingLong(f -> f.toFile().lastModified()));
            Image lastScreen = new ImageIcon(lastFilePath.get().toString()).getImage();

            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new ClipboardImage(lastScreen), null);
            mc.gui.getChat().addMessage(Component.translatable("gui.chrysalis.screenshot_success"));

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
