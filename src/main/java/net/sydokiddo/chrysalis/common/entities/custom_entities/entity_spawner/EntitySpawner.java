package net.sydokiddo.chrysalis.common.entities.custom_entities.entity_spawner;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.CommonColors;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.client.particles.options.ColoredDirectionalDustParticleOptions;
import net.sydokiddo.chrysalis.common.ChrysalisRegistry;
import net.sydokiddo.chrysalis.common.entities.registry.ChrysalisEntities;
import net.sydokiddo.chrysalis.common.misc.ChrysalisSoundEvents;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntitySpawner extends Entity {

    // region Initialization

    private static final String defaultId = Chrysalis.stringId("example");
    public static EntityType<?> entityToSpawn;
    private static Entity displayEntity;
    private long spawnEntityAfterTicks;

    private SoundEvent
        appearSound = ChrysalisSoundEvents.ENTITY_SPAWNER_APPEAR.get(),
        aboutToSpawnEntitySound = ChrysalisSoundEvents.ENTITY_SPAWNER_ABOUT_TO_SPAWN_ENTITY.get(),
        spawnEntitySound = ChrysalisSoundEvents.ENTITY_SPAWNER_SPAWN_ENTITY.get()
    ;

    public EntitySpawner(EntityType<? extends EntitySpawner> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public static void create(Level level, Vec3 position) {
        create(level, null, position);
    }

    public static void create(Level level, String id, Vec3 position) {

        if (level.isClientSide()) return;

        if (id == null) id = defaultId;
        String finalId = id;

        if (Chrysalis.registryAccess == null) return;
        List<EntitySpawnerData.EntitySpawnerConfig> list = Chrysalis.registryAccess.lookupOrThrow(ChrysalisRegistry.ENTITY_SPAWNER_CONFIG_DATA).stream().filter(entitySpawnerConfig -> Objects.equals(entitySpawnerConfig.getId(), finalId)).toList();

        EntitySpawner entitySpawner = new EntitySpawner(ChrysalisEntities.ENTITY_SPAWNER.get(), level);

        for (EntitySpawnerData.EntitySpawnerConfig entitySpawnerConfig : list) {

            if (list.size() > 1) {
                Chrysalis.LOGGER.warn("Detected multiple entity spawner configs of the same id: {}", finalId);
                entitySpawnerConfig = list.getFirst();
            }

            Optional<SpawnData> optionalSpawnData = entitySpawnerConfig.spawnPotentials().getRandomValue(entitySpawner.level().getRandom());
            if (optionalSpawnData.isEmpty()) return;

            Optional<EntityType<?>> optionalEntityType = EntityType.by(optionalSpawnData.get().entityToSpawn());
            if (optionalEntityType.isEmpty()) return;

            entityToSpawn = optionalEntityType.get();
            displayEntity = createEntity(entityToSpawn, level);
            entitySpawner.spawnEntityAfterTicks = level.getRandom().nextIntBetweenInclusive(entitySpawnerConfig.getMinDelay(), entitySpawnerConfig.getMaxDelay());

            entitySpawner.appearSound = entitySpawnerConfig.getAppearSound().value();
            entitySpawner.aboutToSpawnEntitySound = entitySpawnerConfig.getAboutToSpawnEntitySound().value();
            entitySpawner.spawnEntitySound = entitySpawnerConfig.getSpawnEntitySound().value();

            entitySpawner.setAmbientParticleStartingColor(entitySpawnerConfig.getStartingColorFromString());
            entitySpawner.setAmbientParticleEndingColor(entitySpawnerConfig.getEndingColorFromString());
            entitySpawner.setSpawnParticle(entitySpawnerConfig.getSpawnParticle());

            entitySpawner.moveTo(position);
            level.addFreshEntity(entitySpawner);

            playSpawnerSound(level, entitySpawner, entitySpawner.appearSound, 1.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F, true);
        }
    }

    @SuppressWarnings("all")
    private static void playSpawnerSound(Level level, Entity entity, SoundEvent soundEvent, float volume, float pitch, boolean emitGameEvent) {
        level.playSound(null, entity.blockPosition(), soundEvent, SoundSource.NEUTRAL, volume, pitch);
        if (emitGameEvent && level instanceof ServerLevel serverLevel) serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, entity.position());
    }

    // endregion

    // region Entity Data

    private final String
        spawnEntityAfterTicksTag = "spawn_entity_after_ticks",
        ambientParticleStartingColorTag = "ambient_particle_starting_color",
        ambientParticleEndingColorTag = "ambient_particle_ending_color",
        spawnParticleTag = "spawn_particle"
    ;

    private static final EntityDataAccessor<Integer>
        AMBIENT_PARTICLE_STARTING_COLOR = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.INT),
        AMBIENT_PARTICLE_ENDING_COLOR = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.INT)
    ;

    private static final EntityDataAccessor<ParticleOptions> SPAWN_PARTICLE = SynchedEntityData.defineId(EntitySpawner.class, EntityDataSerializers.PARTICLE);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(AMBIENT_PARTICLE_STARTING_COLOR, ComponentHelper.FIRE_COLOR.getRGB());
        builder.define(AMBIENT_PARTICLE_ENDING_COLOR, CommonColors.WHITE);
        builder.define(SPAWN_PARTICLE, ParticleTypes.FLAME);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.putLong(this.spawnEntityAfterTicksTag, this.spawnEntityAfterTicks);
        compoundTag.putInt(this.ambientParticleStartingColorTag, this.getAmbientParticleStartingColor());
        compoundTag.putInt(this.ambientParticleEndingColorTag, this.getAmbientParticleEndingColor());
        compoundTag.put(this.spawnParticleTag, ParticleTypes.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getSpawnParticle()).getOrThrow());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

        this.spawnEntityAfterTicks = compoundTag.getLong(this.spawnEntityAfterTicksTag);
        this.setAmbientParticleStartingColor(compoundTag.getInt(this.ambientParticleStartingColorTag));
        this.setAmbientParticleEndingColor(compoundTag.getInt(this.ambientParticleEndingColorTag));

        if (compoundTag.contains(this.spawnParticleTag, 10)) {
            ParticleTypes.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.spawnParticleTag))
            .resultOrPartial(particle -> Chrysalis.LOGGER.warn("Failed to parse entity spawner particle options: '{}'", particle))
            .ifPresent(this::setSpawnParticle);
        }
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
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverlevel) this.tickServer(serverlevel);
        else this.tickClient();
    }

    private void tickServer(ServerLevel serverLevel) {

        if ((long) this.tickCount == this.spawnEntityAfterTicks - 36L) playSpawnerSound(serverLevel, this, this.aboutToSpawnEntitySound, 1.0F, 1.0F, false);

        if ((long) this.tickCount >= this.spawnEntityAfterTicks) {
            this.trySpawningEntity(serverLevel);
            this.kill(serverLevel);
        }
    }

    private void tickClient() {
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

        if (entityToSpawn == null) return;
        Entity entity = createEntity(entityToSpawn, serverLevel);
        assert entity != null;
        entity.setPos(this.position().x(), this.position().y(), this.position().z());

        serverLevel.addFreshEntity(entity);

        for (int particleAmount = 0; particleAmount < 20; particleAmount++) {
            double xRandomRange = entity.getX() + 0.5D + (this.getRandom().nextDouble() - 0.5D) * 2.0D;
            double yRandomRange = entity.getY() + 0.5D + (this.getRandom().nextDouble() - 0.5D) * 2.0D;
            double zRandomRange = entity.getZ() + 0.5D + (this.getRandom().nextDouble() - 0.5D) * 2.0D;
            serverLevel.sendParticles(this.getSpawnParticle(), xRandomRange, yRandomRange, zRandomRange, 1, 0.0D, 0.0D, 0.0D, 0.05D);
        }

        playSpawnerSound(serverLevel, this, this.spawnEntitySound, 1.0F, (serverLevel.getRandom().nextFloat() - serverLevel.getRandom().nextFloat()) * 0.2F + 1.0F, true);
    }

    private static Entity createEntity(EntityType<?> entityToSpawn, Level level) {
        return entityToSpawn.create(level, EntitySpawnReason.SPAWNER);
    }

    public static Entity getOrCreateDisplayEntity(Level level) {
        if (displayEntity == null && entityToSpawn != null) displayEntity = createEntity(entityToSpawn, level);
        return displayEntity;
    }

    // endregion

    // region Miscellaneous

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float v) {
        return false;
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