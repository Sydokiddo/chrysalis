package net.junebug.chrysalis.common.entities.custom_entities.spawners;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSpawnerEntity extends Entity {

    /**
     * A base spawner entity class that all spawner entities extend.
     **/

    public AbstractSpawnerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    // region Entity Data

    public final int tagType = 10;

    public final String
        entityToSpawnTag = "entity_to_spawn",
        spawnEntitySoundTag = "spawn_entity_sound"
    ;

    public CompoundTag getEntityToSpawn() {
        return new CompoundTag();
    }

    public void setEntityToSpawn(CompoundTag compoundTag) {}

    public Holder<SoundEvent> getSound(EntityDataAccessor<String> soundString) {
        return Holder.direct(SoundEvent.createVariableRangeEvent(ResourceLocation.parse(this.getEntityData().get(soundString))));
    }

    public void setSound(EntityDataAccessor<String> entityDataAccessor, Holder<SoundEvent> soundEvent) {
        this.getEntityData().set(entityDataAccessor, soundEvent.value().location().toString());
    }

    public Holder<SoundEvent> getSpawnEntitySound() {
        return Holder.direct(SoundEvent.createVariableRangeEvent(SoundEvents.EMPTY.location()));
    }

    public void setSpawnEntitySound(Holder<SoundEvent> soundEvent) {}

    // endregion

    // region Ticking

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverlevel) this.tickServer(serverlevel);
        else this.tickClient();
    }

    public void tickServer(ServerLevel serverLevel) {}

    public void tickClient() {}

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float damageAmount) {
        if (!this.isInvulnerableToBase(damageSource)) this.markHurt();
        return false;
    }

    // endregion

    // region Miscellaneous

    public void playSpawnerSound(Level level, Entity entity, SoundEvent soundEvent, float pitch, boolean emitGameEvent) {
        level.playSound(null, entity.blockPosition(), soundEvent, SoundSource.NEUTRAL, 1.0F, pitch);
        if (emitGameEvent && level instanceof ServerLevel serverLevel) serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, entity.position());
    }

    public void emitSpawnParticles(ServerLevel serverLevel, Entity entity, ParticleOptions particleOptions) {
        for (int particleAmount = 0; particleAmount < 20; particleAmount++) {
            double x = entity.getX() + 0.5D + (this.getRandom().nextDouble() - 0.5D) * 2.0D;
            double y = entity.getY() + 0.5D + (this.getRandom().nextDouble() - 0.5D) * 2.0D;
            double z = entity.getZ() + 0.5D + (this.getRandom().nextDouble() - 0.5D) * 2.0D;
            serverLevel.sendParticles(particleOptions, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.05D);
        }
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity entity) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean couldAcceptPassenger() {
        return false;
    }

    @Override
    protected void addPassenger(@NotNull Entity entity) {
        throw new IllegalStateException("Should never addPassenger without checking couldAcceptPassenger()");
    }

    @Override
    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    // endregion
}