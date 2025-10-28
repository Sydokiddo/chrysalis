package net.junebug.chrysalis.common.entities.custom_entities.spawners.entity_spawner;

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
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.client.particles.options.ColoredDirectionalDustParticleOptions;
import net.junebug.chrysalis.common.CRegistry;
import net.junebug.chrysalis.common.entities.custom_entities.spawners.AbstractSpawnerEntity;
import net.junebug.chrysalis.common.entities.registry.CEntities;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.util.helpers.ComponentHelper;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntitySpawner extends AbstractSpawnerEntity {

    /**
     * An entity that plays an animation before spawning a given entity from it.
     **/

    // region Initialization

    private static final String defaultId = Chrysalis.stringId("example");
    private long spawnEntityAfterTicks;

    public EntitySpawner(EntityType<? extends EntitySpawner> entityType, Level level) {
        super(entityType, level);
    }

    public static void create(Level level, Vec3 position) {
        create(level, null, position);
    }

    public static void create(Level level, String id, Vec3 position) {

        if (level.isClientSide()) return;

        if (id == null) id = defaultId;
        String finalId = id;

        if (Chrysalis.registryAccess == null) return;
        List<EntitySpawnerData.EntitySpawnerConfig> list = Chrysalis.registryAccess.lookupOrThrow(CRegistry.ENTITY_SPAWNER_CONFIG_DATA).stream().filter(entitySpawnerConfig -> Objects.equals(entitySpawnerConfig.getId(), finalId)).toList();

        if (list.isEmpty()) {
            Chrysalis.LOGGER.error("Could not find entity spawner config of id: {}", finalId);
            return;
        }

        if (list.size() > 1) Chrysalis.LOGGER.warn("Detected multiple entity spawner configs of the same id: {}", finalId);
        EntitySpawnerData.EntitySpawnerConfig entitySpawnerConfig = list.getFirst();

        EntitySpawner entitySpawner = new EntitySpawner(CEntities.ENTITY_SPAWNER.get(), level);
        Optional<SpawnData> optionalSpawnData = entitySpawnerConfig.spawnPotentials().getRandomValue(entitySpawner.level().getRandom());
        if (optionalSpawnData.isEmpty()) return;

        entitySpawner.setEntityToSpawn(optionalSpawnData.get().entityToSpawn());
        entitySpawner.setSpawnAfterEntityTicks(level.getRandom().nextIntBetweenInclusive(entitySpawnerConfig.getMinDelay(), entitySpawnerConfig.getMaxDelay()));

        entitySpawner.setAppearSound(entitySpawnerConfig.getAppearSound());
        entitySpawner.setAboutToSpawnEntitySound(entitySpawnerConfig.getAboutToSpawnEntitySound());
        entitySpawner.setSpawnEntitySound(entitySpawnerConfig.getSpawnEntitySound());

        entitySpawner.setAmbientParticleStartingColor(entitySpawnerConfig.getStartingColorFromString());
        entitySpawner.setAmbientParticleEndingColor(entitySpawnerConfig.getEndingColorFromString());
        entitySpawner.setSpawnParticle(entitySpawnerConfig.getSpawnParticle());

        entitySpawner.moveTo(position);
        level.addFreshEntity(entitySpawner);

        entitySpawner.playSpawnerSound(level, entitySpawner, entitySpawner.getAppearSound().value(), (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F, true);
    }

    // endregion

    // region Entity Data

    private final String
        spawnEntityAfterTicksTag = "spawn_entity_after_ticks",
        appearSoundTag = "appear_sound",
        aboutToSpawnEntitySoundTag = "about_to_spawn_entity_sound",
        ambientParticleStartingColorTag = "ambient_particle_starting_color",
        ambientParticleEndingColorTag = "ambient_particle_ending_color",
        spawnParticleTag = "spawn_particle"
    ;

    private static final EntityDataAccessor<CompoundTag> ENTITY_TO_SPAWN = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDataAccessor<String>
        APPEAR_SOUND = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.STRING),
        ABOUT_TO_SPAWN_ENTITY_SOUND = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.STRING),
        SPAWN_ENTITY_SOUND = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.STRING)
    ;

    private static final EntityDataAccessor<Integer>
        AMBIENT_PARTICLE_STARTING_COLOR = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.INT),
        AMBIENT_PARTICLE_ENDING_COLOR = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.INT)
    ;

    private static final EntityDataAccessor<ParticleOptions> SPAWN_PARTICLE = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.PARTICLE);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(ENTITY_TO_SPAWN, new CompoundTag());
        this.setSpawnAfterEntityTicks(this.level().getRandom().nextIntBetweenInclusive(60, 120));
        builder.define(APPEAR_SOUND, CSoundEvents.ENTITY_SPAWNER_APPEAR.get().location().toString());
        builder.define(ABOUT_TO_SPAWN_ENTITY_SOUND, CSoundEvents.ENTITY_SPAWNER_ABOUT_TO_SPAWN_ENTITY.get().location().toString());
        builder.define(SPAWN_ENTITY_SOUND, CSoundEvents.ENTITY_SPAWNER_SPAWN_ENTITY.get().location().toString());
        builder.define(AMBIENT_PARTICLE_STARTING_COLOR, ComponentHelper.FIRE_COLOR.getRGB());
        builder.define(AMBIENT_PARTICLE_ENDING_COLOR, CommonColors.WHITE);
        builder.define(SPAWN_PARTICLE, ParticleTypes.FLAME);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        if (!this.getEntityToSpawn().isEmpty()) compoundTag.put(this.entityToSpawnTag, this.getEntityToSpawn());
        compoundTag.putLong(this.spawnEntityAfterTicksTag, this.getSpawnEntityAfterTicks());
        compoundTag.put(this.appearSoundTag, SoundEvent.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getAppearSound()).getOrThrow());
        compoundTag.put(this.aboutToSpawnEntitySoundTag, SoundEvent.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getAboutToSpawnEntitySound()).getOrThrow());
        compoundTag.put(this.spawnEntitySoundTag, SoundEvent.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getSpawnEntitySound()).getOrThrow());
        compoundTag.putInt(this.ambientParticleStartingColorTag, this.getAmbientParticleStartingColor());
        compoundTag.putInt(this.ambientParticleEndingColorTag, this.getAmbientParticleEndingColor());
        compoundTag.put(this.spawnParticleTag, ParticleTypes.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getSpawnParticle()).getOrThrow());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

        if (compoundTag.contains(this.entityToSpawnTag, this.tagType)) this.setEntityToSpawn(compoundTag.getCompound(this.entityToSpawnTag));
        this.setSpawnAfterEntityTicks(compoundTag.getLong(this.spawnEntityAfterTicksTag));

        if (compoundTag.contains(this.appearSoundTag, this.tagType)) {
            SoundEvent.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.appearSoundTag))
            .resultOrPartial(particle -> Chrysalis.LOGGER.warn("Failed to parse entity spawner appear sound: '{}'", particle))
            .ifPresent(this::setAppearSound);
        }

        if (compoundTag.contains(this.aboutToSpawnEntitySoundTag, this.tagType)) {
            SoundEvent.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.aboutToSpawnEntitySoundTag))
            .resultOrPartial(particle -> Chrysalis.LOGGER.warn("Failed to parse entity spawner about to spawn entity sound: '{}'", particle))
            .ifPresent(this::setAboutToSpawnEntitySound);
        }

        if (compoundTag.contains(this.spawnEntitySoundTag, this.tagType)) {
            SoundEvent.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.spawnEntitySoundTag))
            .resultOrPartial(particle -> Chrysalis.LOGGER.warn("Failed to parse entity spawner spawn entity sound: '{}'", particle))
            .ifPresent(this::setSpawnEntitySound);
        }

        this.setAmbientParticleStartingColor(compoundTag.getInt(this.ambientParticleStartingColorTag));
        this.setAmbientParticleEndingColor(compoundTag.getInt(this.ambientParticleEndingColorTag));

        if (compoundTag.contains(this.spawnParticleTag, this.tagType)) {
            ParticleTypes.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.spawnParticleTag))
            .resultOrPartial(particle -> Chrysalis.LOGGER.warn("Failed to parse entity spawner particle options: '{}'", particle))
            .ifPresent(this::setSpawnParticle);
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

    private long getSpawnEntityAfterTicks() {
        return this.spawnEntityAfterTicks;
    }

    private void setSpawnAfterEntityTicks(long spawnEntityAfterTicks) {
        this.spawnEntityAfterTicks = spawnEntityAfterTicks;
    }

    private Holder<SoundEvent> getAppearSound() {
        return this.getSound(APPEAR_SOUND);
    }

    private void setAppearSound(Holder<SoundEvent> soundEvent) {
        this.setSound(APPEAR_SOUND, soundEvent);
    }

    private Holder<SoundEvent> getAboutToSpawnEntitySound() {
        return this.getSound(ABOUT_TO_SPAWN_ENTITY_SOUND);
    }

    private void setAboutToSpawnEntitySound(Holder<SoundEvent> soundEvent) {
        this.setSound(ABOUT_TO_SPAWN_ENTITY_SOUND, soundEvent);
    }

    @Override
    public Holder<SoundEvent> getSpawnEntitySound() {
        return this.getSound(SPAWN_ENTITY_SOUND);
    }

    @Override
    public void setSpawnEntitySound(Holder<SoundEvent> soundEvent) {
        this.setSound(SPAWN_ENTITY_SOUND, soundEvent);
    }

    private int getAmbientParticleStartingColor() {
        return this.getEntityData().get(AMBIENT_PARTICLE_STARTING_COLOR);
    }

    private void setAmbientParticleStartingColor(int startingColor) {
        this.getEntityData().set(AMBIENT_PARTICLE_STARTING_COLOR, startingColor);
    }

    private int getAmbientParticleEndingColor() {
        return this.getEntityData().get(AMBIENT_PARTICLE_ENDING_COLOR);
    }

    private void setAmbientParticleEndingColor(int endingColor) {
        this.getEntityData().set(AMBIENT_PARTICLE_ENDING_COLOR, endingColor);
    }

    private ParticleOptions getSpawnParticle() {
        return this.getEntityData().get(SPAWN_PARTICLE);
    }

    private void setSpawnParticle(ParticleOptions spawnParticle) {
        this.getEntityData().set(SPAWN_PARTICLE, spawnParticle);
    }

    // endregion

    // region Ticking, AI, and Entity Events

    @Override
    public void tickServer(ServerLevel serverLevel) {

        super.tickServer(serverLevel);

        if (this.getEntityToSpawn().isEmpty()) {
            Chrysalis.LOGGER.info("{} has no assigned entity to spawn, despawning it", this.getName().getString());
            this.kill(serverLevel);
        }

        if ((long) this.tickCount == this.getSpawnEntityAfterTicks() - 36L) this.playSpawnerSound(serverLevel, this, this.getAboutToSpawnEntitySound().value(), 1.0F, false);

        if ((long) this.tickCount >= this.getSpawnEntityAfterTicks()) {
            this.trySpawningEntity(serverLevel);
            this.kill(serverLevel);
        }
    }

    @Override
    public void tickClient() {

        super.tickClient();

        if (this.level().getGameTime() % 5L == 0L) {

            Vec3 position = this.position();

            for (int particleAmount = 0; particleAmount < this.getRandom().nextIntBetweenInclusive(3, 6); ++particleAmount) {
                double xRandomRange = 0.4D * (this.getRandom().nextGaussian() - this.getRandom().nextGaussian());
                double yRandomRange = 0.4D * (this.getRandom().nextGaussian() - this.getRandom().nextGaussian());
                double zRandomRange = 0.4D * (this.getRandom().nextGaussian() - this.getRandom().nextGaussian());
                Vec3 vec3 = position.vectorTo(new Vec3(this.getX() + xRandomRange, this.getY() + yRandomRange, this.getZ() + zRandomRange));
                this.level().addParticle(new ColoredDirectionalDustParticleOptions(this.getAmbientParticleStartingColor(), this.getAmbientParticleEndingColor()), position.x(), position.y(), position.z(), vec3.x(), vec3.y(), vec3.z());
            }
        }
    }

    private void trySpawningEntity(ServerLevel serverLevel) {

        if (this.getEntityToSpawn().isEmpty()) return;
        Optional<Entity> entity = createEntity(this);
        if (entity.isEmpty()) return;

        entity.get().setPos(this.position().x(), this.position().y(), this.position().z());
        if (entity.get() instanceof Mob mob) EventHooks.finalizeMobSpawn(mob, serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), EntitySpawnReason.SPAWNER, null);
        serverLevel.addFreshEntity(entity.get());

        this.emitSpawnParticles(serverLevel, entity.get(), this.getSpawnParticle());
        this.playSpawnerSound(serverLevel, this, this.getSpawnEntitySound().value(), (serverLevel.getRandom().nextFloat() - serverLevel.getRandom().nextFloat()) * 0.2F + 1.0F, true);
    }

    public static Optional<Entity> createEntity(EntitySpawner entitySpawner) {
        return EntityType.create(entitySpawner.getEntityToSpawn(), entitySpawner.level(), EntitySpawnReason.SPAWNER);
    }

    // endregion
}