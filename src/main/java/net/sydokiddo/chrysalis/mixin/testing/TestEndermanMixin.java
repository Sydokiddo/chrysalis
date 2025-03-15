package net.sydokiddo.chrysalis.mixin.testing;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.util.entities.interfaces.EncounterMusicMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnderMan.class)
public abstract class TestEndermanMixin extends Monster implements EncounterMusicMob {

    @Shadow public abstract boolean isCreepy();

    private TestEndermanMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Holder<SoundEvent> chrysalis$getEncounterMusic() {
        return SoundEvents.MUSIC_DISC_11;
    }

    @Override
    public boolean chrysalis$shouldStartEncounterMusic() {
        return this.isAlive();
    }

    @Override
    public boolean chrysalis$shouldRefreshEncounterMusic() {
        return this.chrysalis$shouldStartEncounterMusic() && this.isCreepy();
    }
}