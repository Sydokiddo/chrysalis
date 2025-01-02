package net.sydokiddo.chrysalis.misc.util.entities;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.sydokiddo.chrysalis.misc.util.helpers.EventHelper;

public interface EncounterMusicEntity {

    default boolean chrysalis$shouldStartEncounterMusic() {
        return true;
    }

    default boolean chrysalis$shouldRefreshEncounterMusic() {
        return true;
    }

    default Holder<SoundEvent> chrysalis$getEncounterMusic() {
        return SoundEvents.MUSIC_DRAGON;
    }

    default void chrysalis$sendEncounterMusic(Mob mob, boolean playOnFirstTick) {
        EventHelper.sendEncounterMusic(mob, this.chrysalis$getEncounterMusic(), playOnFirstTick);
    }
}