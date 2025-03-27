package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.sounds.MusicInfo;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.util.helpers.EventHelper;
import net.sydokiddo.chrysalis.util.sounds.music.MusicTracker;
import net.sydokiddo.chrysalis.util.technical.splash_texts.CSplashManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Final private User user;
    @Shadow @Nullable public LocalPlayer player;
    @Shadow @Nullable public Screen screen;

    /**
     * Replaces the vanilla splash text manager with Chrysalis's custom one.
     **/

    @Inject(method = "getSplashManager", at = @At("RETURN"), cancellable = true)
    private void chrysalis$getSplashManager(CallbackInfoReturnable<SplashManager> cir) {
        cir.setReturnValue(new CSplashManager(this.user));
    }

    /**
     * Gets situational music from Chrysalis's custom queued music system.
     **/

    @Inject(method = "getSituationalMusic", at = @At("RETURN"), cancellable = true)
    private void chrysalis$getQueuedMusic(CallbackInfoReturnable<MusicInfo> cir) {

        Music music = Optionull.map(this.screen, Screen::getBackgroundMusic);
        if (music == Musics.MENU || Minecraft.getInstance().level == null) MusicTracker.clearMusicOnClient(false);

        if (music != Musics.MENU && music != Musics.CREATIVE && music != Musics.END_BOSS && music != Musics.CREDITS) {
            @Nullable Music queuedMusic = MusicTracker.getQueuedMusic();
            if (queuedMusic != null) cir.setReturnValue(new MusicInfo(queuedMusic));
        }
    }

    /**
     * Plays a sound when the player drops an item.
     **/

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    private void chrysalis$playKeyPressItemDroppingSound(CallbackInfo info) {
        if (this.player != null) EntityDataHelper.playItemDroppingSound(this.player);
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

            if (MusicTracker.fadeOutMusic) {

                this.currentGain = this.currentGain - 0.01F;

                if (this.currentGain <= 0.0F) {
                    MusicTracker.setQueuedMusic(null, true);
                    MusicTracker.setStructureMusic(null, false);
                    MusicTracker.fadeOutMusic = false;
                }
            }

            if (MusicTracker.resetMusicFade) {
                MusicTracker.fadeOutMusic = false;
                this.currentGain = 1.0F;
                MusicTracker.resetMusicFade = false;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(CreativeModeInventoryScreen.class)
    public static class CreativeModeInventoryScreenMixin {

        /**
         * Plays the generic UI click sound whenever a creative mode tab is clicked.
         **/

        @Inject(method = "mouseReleased", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;selectTab(Lnet/minecraft/world/item/CreativeModeTab;)V"))
        private void chrysalis$playCreativeTabClickSound(double x, double y, int keyPressed, CallbackInfoReturnable<Boolean> cir) {
            EventHelper.playUIClickSound(Minecraft.getInstance());
        }
    }
}