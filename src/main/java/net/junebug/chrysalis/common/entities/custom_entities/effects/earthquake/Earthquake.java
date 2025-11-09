package net.junebug.chrysalis.common.entities.custom_entities.effects.earthquake;

import net.junebug.chrysalis.Chrysalis;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.List;

public class Earthquake extends Entity implements TraceableEntity {

    /**
     * An entity that travels in a straight line while emitting particles and damaging most mobs and players in its path.
     **/

    // region Initialization

    public Earthquake(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.hasImpulse = true;
    }

    public static void create(Level level, Vec3 position) {
        create(level, position, 1.0F, 100, 1.2F, 4.0F, 1.0F, CSoundEvents.EARTHQUAKE_TRAVEL, CSoundEvents.EARTHQUAKE_HIT, ParticleTypes.CAMPFIRE_COSY_SMOKE, true);
    }

    public static void create(Level level, Vec3 position, float scale, int lifeTime, float speed, float baseDamage, float baseKnockback, Holder<SoundEvent> travelSound, Holder<SoundEvent> hitSound, ParticleOptions particle, boolean canEmitCameraShake) {

        if (level.isClientSide()) return;

        Earthquake earthquake = new Earthquake(CEntities.EARTHQUAKE.get(), level);

        earthquake.setScale(scale);
        earthquake.setLifeTime(lifeTime);
        earthquake.setSpeed(speed);
        earthquake.setBaseDamage(baseDamage);
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
        baseKnockbackTag = "base_knockback",
        travelSoundTag = "travel_sound",
        hitSoundTag = "hit_sound",
        particleTag = "particle",
        canEmitCameraShakeTag = "can_emit_camera_shake"
    ;

    private static final EntityDataAccessor<Float>
        SCALE = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.FLOAT),
        SPEED = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.FLOAT),
        BASE_DAMAGE = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.FLOAT),
        BASE_KNOCKBACK = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.FLOAT)
    ;

    private static final EntityDataAccessor<Integer> LIFE_TIME = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<String>
        TRAVEL_SOUND = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.STRING),
        HIT_SOUND = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.STRING)
    ;

    private static final EntityDataAccessor<ParticleOptions> PARTICLE = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.PARTICLE);
    private static final EntityDataAccessor<Boolean> CAN_EMIT_CAMERA_SHAKE = SynchedEntityData.defineId(Earthquake.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(SCALE, 1.0F);
        builder.define(LIFE_TIME, 100);
        builder.define(SPEED, 1.2F);
        builder.define(BASE_DAMAGE, 4.0F);
        builder.define(BASE_KNOCKBACK, 1.0F);
        builder.define(TRAVEL_SOUND, CSoundEvents.EARTHQUAKE_TRAVEL.get().location().toString());
        builder.define(HIT_SOUND, CSoundEvents.EARTHQUAKE_HIT.get().location().toString());
        builder.define(PARTICLE, ParticleTypes.CAMPFIRE_COSY_SMOKE);
        builder.define(CAN_EMIT_CAMERA_SHAKE, true);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.putFloat(this.scaleTag, this.getScale());
        compoundTag.putInt(this.lifeTimeTag, this.getLifeTime());
        compoundTag.putFloat(this.speedTag, this.getSpeed());
        compoundTag.putFloat(this.baseDamageTag, this.getBaseDamage());
        compoundTag.putFloat(this.baseKnockbackTag, this.getBaseKnockback());
        compoundTag.put(this.travelSoundTag, SoundEvent.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getTravelSound()).getOrThrow());
        compoundTag.put(this.hitSoundTag, SoundEvent.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getHitSound()).getOrThrow());
        compoundTag.put(this.particleTag, ParticleTypes.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), this.getParticle()).getOrThrow());
        compoundTag.putBoolean(this.canEmitCameraShakeTag, this.canEmitCameraShake());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

        this.setScale(compoundTag.getInt(this.scaleTag));
        this.setLifeTime(compoundTag.getInt(this.lifeTimeTag));
        this.setSpeed(compoundTag.getFloat(this.speedTag));
        this.setBaseDamage(compoundTag.getFloat(this.baseDamageTag));
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
        super.tick();
        this.serverTick();
        this.clientTick();
    }

    private void serverTick() {

        if (this.level().isClientSide()) return;
        if (this.getScale() < 1.0F) this.setScale(1.0F);

        if (this.canEmitCameraShake()) {
            EventHelper.sendCameraShakeToNearbyPlayers(this, null, 10.0D, this.getLifeTime() + 40, 5, 5);
            this.setCanEmitCameraShake(false);
        }

        this.setLifeTime(this.getLifeTime() - 1);

        if (this.getLifeTime() > 0) {

            this.setDeltaMovement(this.getLookAngle().horizontal().normalize().scale(this.getSpeed()));
            this.setPos(this.position().add(this.getDeltaMovement()));

            this.playSound(this.getTravelSound().value(), 1.0F, 0.8F + this.getRandom().nextFloat() * 0.4F);
            this.gameEvent(GameEvent.ENTITY_ACTION);

            if (this.level() instanceof ServerLevel serverLevel) {

                List<? extends LivingEntity> damageRange = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
                LivingEntity livingEntity;

                for (Iterator<? extends LivingEntity> nearbyEntities = damageRange.iterator(); nearbyEntities.hasNext(); this.dealKnockback(livingEntity)) {

                    livingEntity = nearbyEntities.next();

                    if (!livingEntity.getType().is(CTags.IMMUNE_TO_EARTHQUAKES)) {
                        livingEntity.hurtServer(serverLevel, livingEntity.damageSources().source(CDamageTypes.EARTHQUAKE, this), Math.min(this.getBaseDamage() * this.getScale(), Float.MAX_VALUE));
                        this.playSound(this.getHitSound().value(), 1.0F, 0.8F + this.getRandom().nextFloat() * 0.4F);
                    }
                }
            }

        } else {
            this.discard();
        }
    }

    private void dealKnockback(LivingEntity livingEntity) {
        if (livingEntity.getLastDamageSource() != null && !livingEntity.getLastDamageSource().is(DamageTypeTags.NO_KNOCKBACK)) livingEntity.knockback(Math.min(this.getBaseKnockback() * this.getScale(), 20.0F), Mth.sin(this.getYRot() * ((float) Math.PI / 180.0F)), -Mth.cos(this.getYRot() * ((float) Math.PI / 180.0F)));
    }

    private void clientTick() {
        if (!this.level().isClientSide()) return;
        if (this.getBlockStateOn().getRenderShape() != RenderShape.INVISIBLE) this.addParticle(20, 100, new BlockParticleOption(ParticleTypes.BLOCK, this.getBlockStateOn()), 0.0D, 0.0D, 0.0D);
        if (this.tickCount % 2 == 0) this.addParticle(1, 5, this.getParticle(), (Math.random() <= 0.5D) ? 0.025D : -0.025D, 0.025D, (Math.random() <= 0.5D) ? 0.025D : -0.025D);
    }

    private void addParticle(int minAmount, int maxAmount, ParticleOptions particle, double xSpeed, double ySpeed, double zSpeed) {
        for (int particleAmount = 0; particleAmount < this.getRandom().nextIntBetweenInclusive(minAmount * (int) this.getScale(), maxAmount * (int) this.getScale()); ++particleAmount) this.level().addAlwaysVisibleParticle(particle, this.getRandomX(0.5D * this.getScale()), this.getY(), this.getRandomZ(0.5D * this.getScale()), xSpeed, ySpeed, zSpeed);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float damageAmount) {

        if (damageSource.is(CDamageTypes.KILL_WAND)) {
            this.kill(serverLevel);
            return true;
        }

        return false;
    }

    // endregion

    // region Miscellaneous

    @Override
    public @Nullable Entity getOwner() {
        return null;
    }

    @Override
    protected @NotNull AABB makeBoundingBox(@NotNull Vec3 position) {
        return EntityHelper.setHitboxSize(this, this.getScale(), 0.5F);
    }

    private Holder<SoundEvent> getSound(EntityDataAccessor<String> soundString) {
        return Holder.direct(SoundEvent.createVariableRangeEvent(ResourceLocation.parse(this.getEntityData().get(soundString))));
    }

    private void setSound(EntityDataAccessor<String> entityDataAccessor, Holder<SoundEvent> soundEvent) {
        this.getEntityData().set(entityDataAccessor, soundEvent.value().location().toString());
    }

    // endregion
}