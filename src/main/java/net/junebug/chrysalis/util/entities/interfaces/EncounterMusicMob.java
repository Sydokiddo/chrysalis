package net.junebug.chrysalis.util.entities.interfaces;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.junebug.chrysalis.util.helpers.EventHelper;

public interface EncounterMusicMob {

    /**
     * An interface that can be integrated into any mob class to allow for it to emit custom encounter music.
     **/

    default Holder<SoundEvent> getEncounterMusic() {
        return SoundEvents.MUSIC_DRAGON;
    }

    default double getEncounterMusicRange() {
        return 64.0D;
    }

    default double getFinalEncounterMusicRange() {
        return Math.min(this.getEncounterMusicRange(), 128.0D);
    }

    default boolean shouldStartEncounterMusic() {
        return true;
    }

    default boolean shouldRefreshEncounterMusic() {
        return true;
    }

    default void sendEncounterMusic(Mob mob, boolean playOnFirstTick) {
        EventHelper.sendEncounterMusic(mob, this.getEncounterMusic(), playOnFirstTick);
    }
}