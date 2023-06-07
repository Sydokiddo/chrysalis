package net.sydokiddo.chrysalis.misc.util.music_tracker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.sounds.SoundEvent;
import java.util.Random;

@Environment(EnvType.CLIENT)
@SuppressWarnings("ALL")
public class ChrysalisMusicTracker implements SoundEventListener {

    // Structure-specific music code referenced from OST Overhaul by GlowSand, go check it out!

    private final Random random = new Random();
    private final Minecraft minecraft;
    public SoundInstance currentMusic = null;
    private int currentPriority = 0;
    private int timeUntilNextSong = 100;

    public ChrysalisMusicTracker(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void tick() {

        if (!this.minecraft.getSoundManager().isActive(this.currentMusic) || this.minecraft.level == null || this.minecraft.screen instanceof TitleScreen || this.minecraft.screen instanceof CreditsAndAttributionScreen) {
            this.currentMusic = null;
            this.currentPriority = 0;
        }

        this.considerStructure();

        if (this.currentMusic == null) {
            this.timeUntilNextSong--;
        }
    }

    private void play(SoundEvent soundEvent, int priority, int random) {

        if (this.currentPriority <= priority && this.currentMusic == null && this.minecraft.getMusicManager().currentMusic == null) {

            this.minecraft.getSoundManager().stop(this.currentMusic);
            this.timeUntilNextSong = this.random.nextInt(random);
            this.currentPriority = priority;
            this.currentMusic = SimpleSoundInstance.forMusic(soundEvent);

            if (this.currentMusic.getSound() != SoundManager.EMPTY_SOUND) {
                this.minecraft.getSoundManager().play(this.currentMusic);
            }
        }
    }

    public void considerStructure() {}

    @Override
    public void onPlaySound(SoundInstance soundInstance, WeighedSoundEvents weighedSoundEvents) {}
}