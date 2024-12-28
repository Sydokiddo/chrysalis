package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.sydokiddo.chrysalis.misc.util.splash_texts.SplashTextLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SplashRenderer.class)
public class SplashRendererMixin {

    @Shadow @Final private String splash;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void chrysalis$replaceSplashTextRenderer(GuiGraphics guiGraphics, int x, Font font, int y, CallbackInfo info) {
        info.cancel();
        SplashTextLoader.renderSplashText(guiGraphics, x, font, y, true, this.splash, null);
    }
}