package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.data.Main;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(Main.class)
public class MainMixin {

    /**
     * Additional mixin code for the screenshot to clipboard feature.
     **/

    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void chrysalis$setScreenshotProperty(CallbackInfo info) {
        if (!Minecraft.ON_OSX) System.setProperty("java.awt.headless", "false");
    }
}