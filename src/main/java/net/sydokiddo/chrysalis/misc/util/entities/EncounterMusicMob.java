package net.sydokiddo.chrysalis.misc.util.entities;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.sydokiddo.chrysalis.misc.util.helpers.EventHelper;

public interface EncounterMusicMob {

    default Holder<SoundEvent> chrysalis$getEncounterMusic() {
        return SoundEvents.MUSIC_DRAGON;
    }

    default double chrysalis$getEncounterMusicRange() {
        return 64.0D;
    }

    default double chrysalis$getFinalEncounterMusicRange() {
        return Math.min(this.chrysalis$getEncounterMusicRange(), 128.0D);
    }

    default boolean chrysalis$shouldStartEncounterMusic() {
        return true;
    }

    default boolean chrysalis$shouldRefreshEncounterMusic() {
        return true;
    }

    default void chrysalis$sendEncounterMusic(Mob mob, boolean playOnFirstTick) {
        EventHelper.sendEncounterMusic(mob, this.chrysalis$getEncounterMusic(), playOnFirstTick);
    }
}