package net.sydokiddo.chrysalis.common.entities.custom_entities.spawners.encounter_spawner;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.EventHooks;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.entities.custom_entities.spawners.AbstractSpawnerEntity;
import net.sydokiddo.chrysalis.common.misc.ChrysalisDamageTypes;
import net.sydokiddo.chrysalis.common.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public class EncounterSpawner extends AbstractSpawnerEntity {

    // region Initialization

    public EncounterSpawner(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    // endregion

    // region Entity Data

    private final String particleTag = "particle";

    private static final EntityDataAccessor<CompoundTag> ENTITY_TO_SPAWN = SynchedEntityData.defineId(EncounterSpawner.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<String> SPAWN_ENTITY_SOUND = SynchedEntityData.defineId(EncounterSpawner.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<ParticleOptions> PARTICLE = SynchedEntityData.defineId(EncounterSpawner.class, EntityDataSerializers.PARTICLE);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(ENTITY_TO_SPAWN, new CompoundTag());
        builder.define(SPAWN_ENTITY_SOUND, ChrysalisSoundEvents.ENCOUNTER_SPAWNER_SPAWN_ENTITY.get().location().toString());
        builder.define(PARTICLE, ParticleTypes.FLAME);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        if (!this.getEntityToSpawn().isEmpty()) compoundTag.put(this.entityToSpawnTag, this.getEntityToSpawn());
        compoundTag.put(this.spawnEntitySoundTag, SoundEvent.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getSpawnEntitySound()).getOrThrow());
        compoundTag.put(this.particleTag, ParticleTypes.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getParticle()).getOrThrow());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

        if (compoundTag.contains(this.entityToSpawnTag, this.tagType)) this.setEntityToSpawn(compoundTag.getCompound(this.entityToSpawnTag));

        if (compoundTag.contains(this.spawnEntitySoundTag, this.tagType)) {
            SoundEvent.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.spawnEntitySoundTag))
            .resultOrPartial(particle -> Chrysalis.LOGGER.warn("Failed to parse encounter spawner spawn entity sound: '{}'", particle))
            .ifPresent(this::setSpawnEntitySound);
        }

        if (compoundTag.contains(this.particleTag, this.tagType)) {
            ParticleTypes.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.particleTag))
            .resultOrPartial(particle -> Chrysalis.LOGGER.warn("Failed to parse encounter spawner particle options: '{}'", particle))
            .ifPresent(this::setParticle);
        }
    }

    @Override
    public CompoundTag getEntityToSpawn() {
        return this.getEntityData().get(ENTITY_TO_SPAWN);
    }

    @Override
    public void setEntityToSpawn(CompoundTag compoundTag) {
        this.getEntityData().set(ENTITY_TO_SPAWN, compoundTag);
    }

    @Override
    public Holder<SoundEvent> getSpawnEntitySound() {
        return this.getSound(SPAWN_ENTITY_SOUND);
    }

    @Override
    public void setSpawnEntitySound(Holder<SoundEvent> soundEvent) {
        this.setSound(SPAWN_ENTITY_SOUND, soundEvent);
    }

    private ParticleOptions getParticle() {
        return this.getEntityData().get(PARTICLE);
    }

    private void setParticle(ParticleOptions spawnParticle) {
        this.getEntityData().set(PARTICLE, spawnParticle);
    }

    // endregion

    // region Ticking, AI, and Entity Events

    @Override
    public void tickServer(ServerLevel serverLevel) {

        super.tickServer(serverLevel);

        if (this.hasNearbyPlayer() && !this.getEntityToSpawn().isEmpty()) {
            this.trySpawningEntity(serverLevel);
            this.kill(serverLevel);
        }
    }

    private boolean hasNearbyPlayer() {

        double distance = 16.0D;

        for (Player player : this.level().players()) {
            if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(player)) {
                if (player.distanceToSqr(this.getX() + 0.5D, this.getY() + 0.5D, this.getZ() + 0.5D) < distance * distance) return true;
            }
        }

        return false;
    }

    @Override
    public void tickClient() {

        super.tickClient();

        if (this.level().getGameTime() % 20L == 0) {
            for (int particleAmount = 0; particleAmount < this.getRandom().nextIntBetweenInclusive(1, 3); ++particleAmount) {
                this.level().addParticle(this.getParticle(), this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), this.getRandom().nextFloat() * 0.05F, this.getRandom().nextFloat() * 0.05F, this.getRandom().nextFloat() * 0.05F);
            }
        }
    }

    private void trySpawningEntity(ServerLevel serverLevel) {

        Optional<Entity> entity = createEntity(this);
        if (entity.isEmpty()) return;

        entity.get().setPos(this.position().x(), this.position().y(), this.position().z());
        if (entity.get() instanceof Mob mob) EventHooks.finalizeMobSpawn(mob, serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), EntitySpawnReason.SPAWNER, null);
        serverLevel.addFreshEntity(entity.get());

        this.emitSpawnParticles(serverLevel, entity.get(), this.getParticle());
        this.playSpawnerSound(serverLevel, this, this.getSpawnEntitySound().value(), (serverLevel.getRandom().nextFloat() - serverLevel.getRandom().nextFloat()) * 0.2F + 1.0F, true);
    }

    public static Optional<Entity> createEntity(EncounterSpawner encounterSpawner) {
        if (encounterSpawner.getEntityToSpawn().isEmpty()) return Optional.empty();
        return EntityType.create(encounterSpawner.getEntityToSpawn(), encounterSpawner.level(), EntitySpawnReason.SPAWNER);
    }

    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand interactionHand) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);

        if (!player.level().isClientSide() && !player.isSecondaryUseActive() && itemInHand.getItem() instanceof SpawnEggItem spawnEggItem) {

            EntityType<?> entityType = spawnEggItem.getType(player.level().registryAccess(), itemInHand);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("id", entityType.toShortString());

            this.setEntityToSpawn(compoundTag);
            this.playSpawnerSound(player.level(), this, ChrysalisSoundEvents.GENERIC_SPAWNER_CHANGE_ENTITY.get(), 1.0F, false);

            player.level().gameEvent(player, GameEvent.BLOCK_CHANGE, this.blockPosition());
            itemInHand.shrink(1);

            return InteractionResult.SUCCESS_SERVER;
        }

        return super.interact(player, interactionHand);
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public float getPickRadius() {
        return 0.5F;
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float damageAmount) {

        if (damageSource.is(ChrysalisDamageTypes.KILL_WAND)) {
            this.kill(serverLevel);
            return true;
        }

        return super.hurtServer(serverLevel, damageSource, damageAmount);
    }

    // endregion
}