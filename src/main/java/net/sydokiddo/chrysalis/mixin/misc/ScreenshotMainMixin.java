package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class ScreenshotMainMixin {

    /**
     * Additional mixin code for the screenshot to clipboard feature.
     **/

    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void chrysalis_setScreenshotProperty(CallbackInfo ci) {
        System.setProperty("java.awt.headless", "false");
    }
}