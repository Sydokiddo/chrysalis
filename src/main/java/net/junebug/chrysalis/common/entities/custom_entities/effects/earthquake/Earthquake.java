package net.junebug.chrysalis.common.entities.custom_entities.effects.earthquake;

import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.client.particles.options.DustCloudParticleOptions;
import net.junebug.chrysalis.client.particles.types.DustCloudParticle;
import net.junebug.chrysalis.common.entities.registry.CEntities;
import net.junebug.chrysalis.common.misc.CDamageTypes;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.util.helpers.EntityHelper;
import net.junebug.chrysalis.util.helpers.EventHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Earthquake extends Entity implements TraceableEntity {

    /**
     * An entity that travels in a straight line while emitting particles and damaging most mobs and players in its path.
     **/

    // region Initialization

    public Earthquake(EntityType<? extends Earthquake> entityType, Level level) {
        super(entityType, level);
        this.hasImpulse = true;
    }

    @Nullable private Entity owner;
    @Nullable private UUID ownerUUID;

    private static final float
        defaultScale = 1.0F,
        defaultSpeed = 1.0F,
        defaultBaseDamage = 4.0F,
        defaultBaseKnockback = 2.0F
    ;

    private static final boolean
        defaultDamageScalesFromDifficulty = true,
        defaultCanEmitCameraShake = true
    ;

    private static final Holder<SoundEvent>
        defaultTravelSound = CSoundEvents.EARTHQUAKE_TRAVEL,
        defaultHitSound = CSoundEvents.EARTHQUAKE_HIT
    ;

    private static final int defaultLifeTime = 20;
    private static final ParticleOptions defaultParticle = new DustCloudParticleOptions(DustCloudParticle.defaultColor, false);

    public static void create(Level level, LivingEntity owner, Vec3 position, float yRot, float xRot) {
        create(level, owner, position, yRot, xRot, defaultScale, defaultLifeTime, defaultSpeed, defaultBaseDamage, defaultDamageScalesFromDifficulty, defaultBaseKnockback, defaultTravelSound, defaultHitSound, defaultParticle, defaultCanEmitCameraShake);
    }

    public static void create(Level level, LivingEntity owner, Vec3 position, float yRot, float xRot, float scale, int lifeTime, float speed, float baseDamage, boolean damageScalesFromDifficulty, float baseKnockback, Holder<SoundEvent> travelSound, Holder<SoundEvent> hitSound, ParticleOptions particle, boolean canEmitCameraShake) {

        if (level.isClientSide()) return;

        Earthquake earthquake = new Earthquake(CEntities.EARTHQUAKE.get(), level);

        earthquake.setOwner(owner);
        earthquake.setRot(yRot, xRot);
        earthquake.setScale(scale);
        earthquake.setLifeTime(lifeTime);
        earthquake.setSpeed(speed);
        earthquake.setBaseDamage(baseDamage);
        earthquake.setDamageScalesFromDifficulty(damageScalesFromDifficulty);
        earthquake.setBaseKnockback(baseKnockback);
        earthquake.setTravelSound(travelSound);
        earthquake.setHitSound(hitSound);
        earthquake.setParticle(particle);
        earthquake.setCanEmitCameraShake(canEmitCameraShake);

        earthquake.moveTo(position);
        level.addFreshEntity(earthquake);
    }

    // endregion

    // region Entity Data

    private final String
        scaleTag = "scale",
        lifeTimeTag = "life_time",
        speedTag = "speed",
        baseDamageTag = "base_damage",
        damageScalesFromDifficultyTag = "damage_scales_from_difficulty",
        baseKnockbackTag = "base_knockback",
        travelSoundTag = "travel_sound",
        hitSoundTag = "hit_sound",
        particleTag = "particle",
        canEmitCameraShakeTag = "can_emit_camera_shake",
        ownerTag = "owner"
    ;

    private static final EntityDataAccessor<Float>
        SCALE = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.FLOAT),
        SPEED = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.FLOAT),
        BASE_DAMAGE = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.FLOAT),
        BASE_KNOCKBACK = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.FLOAT)
    ;

    private static final EntityDataAccessor<Boolean>
        DAMAGE_SCALES_FROM_DIFFICULTY = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.BOOLEAN),
        CAN_EMIT_CAMERA_SHAKE = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.BOOLEAN)
    ;

    private static final EntityDataAccessor<String>
        TRAVEL_SOUND = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.STRING),
        HIT_SOUND = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.STRING)
    ;

    private static final EntityDataAccessor<Integer> LIFE_TIME = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ParticleOptions> PARTICLE = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.PARTICLE);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(SCALE, defaultScale);
        builder.define(LIFE_TIME, defaultLifeTime);
        builder.define(SPEED, defaultSpeed);
        builder.define(BASE_DAMAGE, defaultBaseDamage);
        builder.define(DAMAGE_SCALES_FROM_DIFFICULTY, defaultDamageScalesFromDifficulty);
        builder.define(BASE_KNOCKBACK, defaultBaseKnockback);
        builder.define(TRAVEL_SOUND, defaultTravelSound.value().location().toString());
        builder.define(HIT_SOUND, defaultHitSound.value().location().toString());
        builder.define(PARTICLE, defaultParticle);
        builder.define(CAN_EMIT_CAMERA_SHAKE, defaultCanEmitCameraShake);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.putFloat(this.scaleTag, this.getScale());
        compoundTag.putInt(this.lifeTimeTag, this.getLifeTime());
        compoundTag.putFloat(this.speedTag, this.getSpeed());
        compoundTag.putFloat(this.baseDamageTag, this.getBaseDamage());
        compoundTag.putBoolean(this.damageScalesFromDifficultyTag, this.damageScalesFromDifficulty());
        compoundTag.putFloat(this.baseKnockbackTag, this.getBaseKnockback());
        compoundTag.put(this.travelSoundTag, SoundEvent.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getTravelSound()).getOrThrow());
        compoundTag.put(this.hitSoundTag, SoundEvent.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getHitSound()).getOrThrow());
        compoundTag.put(this.particleTag, ParticleTypes.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getParticle()).getOrThrow());
        compoundTag.putBoolean(this.canEmitCameraShakeTag, this.canEmitCameraShake());
        if (this.ownerUUID != null) compoundTag.putUUID(this.ownerTag, this.ownerUUID);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

        this.setScale(compoundTag.getInt(this.scaleTag));
        this.setLifeTime(compoundTag.getInt(this.lifeTimeTag));
        this.setSpeed(compoundTag.getFloat(this.speedTag));
        this.setBaseDamage(compoundTag.getFloat(this.baseDamageTag));
        this.setDamageScalesFromDifficulty(compoundTag.getBoolean(this.damageScalesFromDifficultyTag));
        this.setBaseKnockback(compoundTag.getFloat(this.baseKnockbackTag));

        int tagType = 10;

        if (compoundTag.contains(this.travelSoundTag, tagType)) {
            SoundEvent.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.travelSoundTag))
            .resultOrPartial(soundEvent -> Chrysalis.LOGGER.warn("Failed to parse earthquake travel sound: '{}'", soundEvent))
            .ifPresent(this::setTravelSound);
        }

        if (compoundTag.contains(this.hitSoundTag, tagType)) {
            SoundEvent.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.hitSoundTag))
            .resultOrPartial(soundEvent -> Chrysalis.LOGGER.warn("Failed to parse earthquake hit sound: '{}'", soundEvent))
            .ifPresent(this::setHitSound);
        }

        if (compoundTag.contains(this.particleTag, tagType)) {
            ParticleTypes.CODEC
            .parse(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), compoundTag.get(this.particleTag))
            .resultOrPartial(particle -> Chrysalis.LOGGER.warn("Failed to parse earthquake particle options: '{}'", particle))
            .ifPresent(this::setParticle);
        }

        this.setCanEmitCameraShake(compoundTag.getBoolean(this.canEmitCameraShakeTag));
        if (compoundTag.hasUUID(this.ownerTag)) this.ownerUUID = compoundTag.getUUID(this.ownerTag);
    }

    private float getScale() {
        return this.getEntityData().get(SCALE);
    }

    private void setScale(float scale) {
        this.getEntityData().set(SCALE, scale);
    }

    private int getLifeTime() {
        return this.getEntityData().get(LIFE_TIME);
    }

    private void setLifeTime(int lifeTime) {
        this.getEntityData().set(LIFE_TIME, lifeTime);
    }

    private boolean damageScalesFromDifficulty() {
        return this.getEntityData().get(DAMAGE_SCALES_FROM_DIFFICULTY);
    }

    private void setDamageScalesFromDifficulty(boolean damageScalesFromDifficulty) {
        this.getEntityData().set(DAMAGE_SCALES_FROM_DIFFICULTY, damageScalesFromDifficulty);
    }

    private float getSpeed() {
        return this.getEntityData().get(SPEED);
    }

    private void setSpeed(float speed) {
        this.getEntityData().set(SPEED, speed);
    }

    private float getBaseDamage() {
        return this.getEntityData().get(BASE_DAMAGE);
    }

    private void setBaseDamage(float baseDamage) {
        this.getEntityData().set(BASE_DAMAGE, baseDamage);
    }

    private float getBaseKnockback() {
        return this.getEntityData().get(BASE_KNOCKBACK);
    }

    private void setBaseKnockback(float baseKnockback) {
        this.getEntityData().set(BASE_KNOCKBACK, baseKnockback);
    }

    private Holder<SoundEvent> getTravelSound() {
        return this.getSound(TRAVEL_SOUND);
    }

    private void setTravelSound(Holder<SoundEvent> soundEvent) {
        this.setSound(TRAVEL_SOUND, soundEvent);
    }

    private Holder<SoundEvent> getHitSound() {
        return this.getSound(HIT_SOUND);
    }

    private void setHitSound(Holder<SoundEvent> soundEvent) {
        this.setSound(HIT_SOUND, soundEvent);
    }

    private ParticleOptions getParticle() {
        return this.getEntityData().get(PARTICLE);
    }

    private void setParticle(ParticleOptions particle) {
        this.getEntityData().set(PARTICLE, particle);
    }

    private boolean canEmitCameraShake() {
        return this.getEntityData().get(CAN_EMIT_CAMERA_SHAKE);
    }

    private void setCanEmitCameraShake(boolean canEmitCameraShake) {
        this.getEntityData().set(CAN_EMIT_CAMERA_SHAKE, canEmitCameraShake);
    }

    // endregion

    // region Ticking, AI, and Entity Events

    @Override
    public void tick() {
        this.serverTick();
        this.clientTick();
        super.tick();
    }

    private float startingScale;

    private void serverTick() {

        if (this.level().isClientSide()) return;

        if (this.firstTick) {
            if (this.getScale() < 1.0F) this.setScale(1.0F);
            this.startingScale = this.getScale();
        }

        if (this.canEmitCameraShake()) {
            EventHelper.sendCameraShakeToNearbyPlayers(this, null, 10.0D, this.getLifeTime() + 100, 5, 5);
            this.setCanEmitCameraShake(false);
        }

        this.setLifeTime(this.getLifeTime() - 1);

        float damageAmount = Math.max(Math.min(Math.round((this.getBaseDamage() * this.getScale()) * (this.getLifeTime() / 20.0F)) * (this.damageScalesFromDifficulty() ? this.level().getDifficulty().getId() / 2.0F : 1.0F), Float.MAX_VALUE), 1.0F);
        float knockbackAmount = Math.max(Math.min(Math.round((this.getBaseKnockback() * this.getScale()) * (this.getLifeTime() / 20.0F)), 20.0F), 0.0F);

        if (this.getLifeTime() >= 0) {

            this.setDeltaMovement(this.getLookAngle().horizontal().normalize().scale(this.getSpeed()));
            if (this.horizontalCollision) this.setDeltaMovement(this.getDeltaMovement().x(), this.maxUpStep(), this.getDeltaMovement().z());
            if (this.getBlockStateOn().getCollisionShape(this.level(), this.getOnPos()).isEmpty()) this.setDeltaMovement(this.getDeltaMovement().x(), -this.getDefaultGravity(), this.getDeltaMovement().z());
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.scaleSize(false, 0.05F * this.startingScale);

            this.playSound(this.getTravelSound().value(), 1.0F, 0.8F + this.getRandom().nextFloat() * 0.4F);
            this.gameEvent(GameEvent.BLOCK_DESTROY);

            if (this.level() instanceof ServerLevel serverLevel) {

                List<? extends LivingEntity> damageRange = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
                LivingEntity livingEntity;

                for (Iterator<? extends LivingEntity> nearbyEntities = damageRange.iterator(); nearbyEntities.hasNext(); this.dealKnockback(livingEntity, knockbackAmount)) {

                    livingEntity = nearbyEntities.next();

                    if (!livingEntity.getType().is(CTags.IMMUNE_TO_EARTHQUAKES) && this.getOwner() != livingEntity) {
                        livingEntity.hurtServer(serverLevel, livingEntity.damageSources().source(CDamageTypes.EARTHQUAKE, this.getOwner()), damageAmount);
                        this.playSound(this.getHitSound().value(), 1.0F, 0.8F + this.getRandom().nextFloat() * 0.4F);
                        if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Dealt {} earthquake damage to {}", damageAmount, livingEntity.getName().getString());
                    }
                }
            }

        } else {
            this.scaleSize(true, 0.35F * this.startingScale);
        }
    }

    private void dealKnockback(LivingEntity livingEntity, float knockbackAmount) {
        if (livingEntity.getLastDamageSource() != null && !livingEntity.getLastDamageSource().is(DamageTypeTags.NO_KNOCKBACK) && !livingEntity.isDeadOrDying()) livingEntity.knockback(knockbackAmount, Mth.sin(this.getYRot() * ((float) Math.PI / 180.0F)), -Mth.cos(this.getYRot() * ((float) Math.PI / 180.0F)));
    }

    private void scaleSize(boolean shrink, float scaleAmount) {
        if (this.getScale() > 0.05F) {
            if (shrink) this.setScale(this.getScale() - scaleAmount);
            else this.setScale(this.getScale() + scaleAmount);
        } else {
            this.discard();
        }
    }

    private void clientTick() {

        if (!this.level().isClientSide()) return;

        if (this.getBlockStateOn().getRenderShape() != RenderShape.INVISIBLE) {
            this.addParticle(20, 100, new BlockParticleOption(ParticleTypes.BLOCK, this.getBlockStateOn()), 0.0D, 0.0D, 0.0D);
            if (this.tickCount % 2 == 0) this.addParticle(1, 5, this.getParticle(), (Math.random() <= 0.5D) ? 0.025D : -0.025D, 0.025D, (Math.random() <= 0.5D) ? 0.025D : -0.025D);
        }
    }

    private void addParticle(int minAmount, int maxAmount, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed) {

        int multiplier;
        if (this.getScale() < 1.0F) multiplier = 1;
        else multiplier = (int) this.getScale();

        for (int particleAmount = 0; particleAmount < this.getRandom().nextIntBetweenInclusive(minAmount * multiplier, maxAmount * multiplier); ++particleAmount) this.level().addAlwaysVisibleParticle(particle, this.getRandomX(0.5D * this.getScale()), this.getY(), this.getRandomZ(0.5D * this.getScale()), xSpeed, ySpeed, zSpeed);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float damageAmount) {
        return false;
    }

    // endregion

    // region Miscellaneous

    @Override
    public @Nullable Entity getOwner() {

        if (this.owner == null && this.ownerUUID == null) this.owner = this;

        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {

            Entity owner;

            if (serverLevel.getEntity(this.ownerUUID) instanceof LivingEntity livingEntity) owner = livingEntity;
            else owner = this;

            this.owner = owner;
        }

        return this.owner;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? this.getUUID() : owner.getUUID();
    }

    @Override
    protected @NotNull AABB makeBoundingBox(@NotNull Vec3 position) {
        return EntityHelper.setHitboxSize(this, this.getScale(), 0.5F);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (SCALE.equals(key)) this.refreshDimensions();
        super.onSyncedDataUpdated(key);
    }

    @Override
    public float maxUpStep() {
        return 1.0F;
    }

    @Override
    protected double getDefaultGravity() {
        return 1.0F;
    }

    @Override
    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    private Holder<SoundEvent> getSound(EntityDataAccessor<String> soundString) {
        return Holder.direct(SoundEvent.createVariableRangeEvent(ResourceLocation.parse(this.getEntityData().get(soundString))));
    }

    private void setSound(EntityDataAccessor<String> entityDataAccessor, Holder<SoundEvent> soundEvent) {
        this.getEntityData().set(entityDataAccessor, soundEvent.value().location().toString());
    }

    // endregion
}