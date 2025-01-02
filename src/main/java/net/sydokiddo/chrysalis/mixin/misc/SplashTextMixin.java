package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.splash_texts.SplashTextLoader;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(SplashRenderer.class)
public class SplashTextMixin {

    @Shadow @Final private String splash;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void chrysalis$replaceSplashTextRenderer(GuiGraphics guiGraphics, int x, Font font, int y, CallbackInfo info) {
        info.cancel();
        SplashTextLoader.renderSplashText(guiGraphics, x, font, y, true, this.splash, null);
    }

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    @Mixin(TitleScreen.class)
    public static class TitleScreenMixin {

        @Shadow private @Nullable SplashRenderer splash;

        @Inject(method = "mouseClicked", at = @At("TAIL"))
        private void chrysalis$splashTextRefreshing(double x, double y, int z, CallbackInfoReturnable<Boolean> cir) {

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.screen == null) return;

            float splashTextX = minecraft.screen.width / 2.0F + 123.0F;
            float splashTextY = 69.0F;

            float maxRangeX = 50.0F;
            float maxRangeY = 25.0F;
            if (x > splashTextX + maxRangeX || x < splashTextX - maxRangeX || y > splashTextY + maxRangeY || y < splashTextY - maxRangeY) return;

            if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Splash text has been refreshed");
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(ChrysalisSoundEvents.SPLASH_TEXT_SHUFFLE, 1.0F));
            this.splash = null;
            this.splash = minecraft.getSplashManager().getSplash();
            SplashTextLoader.lifeTime = 0;
        }
    }
}