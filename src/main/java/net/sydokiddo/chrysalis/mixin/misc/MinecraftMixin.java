package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.sounds.MusicInfo;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.sydokiddo.chrysalis.client.ChrysalisClient;
import net.sydokiddo.chrysalis.misc.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.misc.util.splash_texts.CSplashManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Final private User user;
    @Shadow @Nullable public LocalPlayer player;
    @Shadow @Nullable public Screen screen;

    @Inject(method = "getSplashManager", at = @At("RETURN"), cancellable = true)
    private void chrysalis$getSplashManager(CallbackInfoReturnable<SplashManager> cir) {
        cir.setReturnValue(new CSplashManager(this.user));
    }

    @Inject(method = "getSituationalMusic", at = @At("RETURN"), cancellable = true)
    private void chrysalis$getQueuedMusic(CallbackInfoReturnable<MusicInfo> cir) {

        Music music = Optionull.map(this.screen, Screen::getBackgroundMusic);
        if (music == Musics.MENU || Minecraft.getInstance().level == null) ChrysalisClient.clearMusicOnClient(false);

        if (music != Musics.MENU && music != Musics.CREATIVE && music != Musics.END_BOSS && music != Musics.CREDITS) {
            @Nullable Music queuedMusic = ChrysalisClient.getQueuedMusic();
            if (queuedMusic != null) cir.setReturnValue(new MusicInfo(queuedMusic));
        }
    }

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"))
    private void chrysalis$playKeyPressItemDroppingSound(CallbackInfo info) {
        if (this.player != null) EntityDataHelper.playItemDroppingSound(this.player);
    }

    @SuppressWarnings("unused")
    @Environment(EnvType.CLIENT)
    @Mixin(MusicManager.class)
    public static class MusicManagerMixin {

        @Shadow private float currentGain;

        @Inject(method = "tick", at = @At("HEAD"))
        private void chrysalis$fadeOutMusic(CallbackInfo info) {
            if (ChrysalisClient.fadeOutMusic) {

                this.currentGain = this.currentGain - 0.01F;

                if (this.currentGain <= 0.0F) {
                    ChrysalisClient.setQueuedMusic(null, true);
                    ChrysalisClient.setStructureMusic(null, false);
                    ChrysalisClient.fadeOutMusic = false;
                }
            }
        }
    }
}