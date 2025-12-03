package net.junebug.chrysalis.mixin.misc;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.User;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.sounds.MusicInfo;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ScreenshotEvent;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.events.CClientEvents;
import net.junebug.chrysalis.util.helpers.EventHelper;
import net.junebug.chrysalis.util.sounds.music.MusicTracker;
import net.junebug.chrysalis.util.technical.ClipboardImage;
import net.junebug.chrysalis.util.technical.splash_texts.CSplashManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Final private User user;
    @Shadow @Nullable public LocalPlayer player;
    @Shadow @Nullable public Screen screen;

    /**
     * Replaces the vanilla splash text manager with chrysalis's custom one.
     **/

    @Inject(method = "getSplashManager", at = @At("RETURN"), cancellable = true)
    private void chrysalis$getSplashManager(CallbackInfoReturnable<SplashManager> cir) {
        cir.setReturnValue(new CSplashManager(this.user));
    }

    /**
     * Gets situational music from chrysalis's custom queued music system.
     **/

    @Inject(method = "getSituationalMusic", at = @At("RETURN"), cancellable = true)
    private void chrysalis$getQueuedMusic(CallbackInfoReturnable<MusicInfo> cir) {

        Music music = Optionull.map(this.screen, Screen::getBackgroundMusic);
        if (music == Musics.MENU || Minecraft.getInstance().level == null) MusicTracker.onClient.clearMusic(false);

        if (music != Musics.MENU && music != Musics.CREATIVE && music != Musics.END_BOSS && music != Musics.CREDITS) {
            @Nullable Music queuedMusic = MusicTracker.onClient.getQueuedMusic();
            if (queuedMusic != null) cir.setReturnValue(new MusicInfo(queuedMusic));
        }
    }

    /**
     * Plays a sound when the player drops an item.
     **/

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    private void chrysalis$playKeyPressItemDroppingSound(CallbackInfo info) {
        if (this.player != null) EventHelper.playItemDroppingSound(this.player);
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(MusicManager.class)
    public static class MusicManagerMixin {

        @Shadow private float currentGain;

        /**
         * Fades out the current played music if instructed to do so.
         **/

        @Inject(method = "tick", at = @At("HEAD"))
        private void chrysalis$fadeOutMusic(CallbackInfo info) {

            if (MusicTracker.onClient.fadeOutMusic) {

                this.currentGain = this.currentGain - 0.01F;

                if (this.currentGain <= 0.0F) {
                    MusicTracker.onClient.setQueuedMusic(null, true);
                    MusicTracker.onClient.setStructureMusic(null, false);
                    MusicTracker.onClient.fadeOutMusic = false;
                }
            }

            if (MusicTracker.onClient.resetMusicFade) {
                MusicTracker.onClient.fadeOutMusic = false;
                this.currentGain = 1.0F;
                MusicTracker.onClient.resetMusicFade = false;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(Screenshot.class)
    public static class ScreenshotMixin {

        /**
         * Copies screenshots to the computer's clipboard.
         **/

        @Inject(method = "lambda$_grab$2", at = @At("TAIL"))
        private static void chrysalis$copyScreenshotToClipboard(NativeImage nativeimage, File file, ScreenshotEvent event, Consumer<?> messageConsumer, CallbackInfo info) {

            if (!CClientEvents.GameEventBus.canPlayScreenshotEvents(event, true)) return;

            try {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new ClipboardImage(new ImageIcon(event.getScreenshotFile().toString()).getImage()), null);
                Minecraft minecraft = Minecraft.getInstance();
                Component message = Component.translatable("gui.chrysalis.screenshot_copied");
                minecraft.gui.getChat().addMessage(message);
                minecraft.getNarrator().sayNow(message);
            } catch (Exception exception) {
                Chrysalis.LOGGER.warn("Failed to copy screenshot to clipboard", exception);
            }
        }
    }
}