package net.sydokiddo.chrysalis.mixin.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.sydokiddo.chrysalis.misc.util.music_tracker.ChrysalisMusicTracker;
import net.sydokiddo.chrysalis.misc.util.music_tracker.MusicTrackerInterface;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin implements MusicTrackerInterface {

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Nullable public SoundInstance currentMusic;
    @Unique private ChrysalisMusicTracker musicTracker;

    @Inject(method = "<init>", at= @At("TAIL"))
    private void chrysalis_musicManagerInitMixin(Minecraft minecraft, CallbackInfo ci) {
        musicTracker = new ChrysalisMusicTracker(minecraft);
        this.minecraft.getSoundManager().addListener(musicTracker);
    }

    @Inject(method = "tick",at = @At("HEAD"))
    private void chrysalis_musicManagerTickMixin(CallbackInfo ci) {

        // If music isn't already playing, tick the custom music tracker

        if (this.currentMusic == null) {
            this.musicTracker.tick();
        }
    }

    @Inject(method = "startPlaying",at = @At("HEAD"), cancellable = true)
    private void chrysalis_cancelMusic(Music music, CallbackInfo ci) {

        // If music from the custom music tracker is playing, cancel the default game music from playing

        if (musicTracker.currentMusic != null || this.minecraft.getSoundManager().isActive(musicTracker.currentMusic)) {
            ci.cancel();
        }
    }

    @Override
    public ChrysalisMusicTracker getTracker() {
        return this.musicTracker;
    }
}