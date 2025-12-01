package net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem;

import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.client.particles.options.DustCloudParticleOptions;
import net.junebug.chrysalis.client.particles.options.RotatingDustParticleOptions;
import net.junebug.chrysalis.client.particles.options.SparkParticleOptions;
import net.junebug.chrysalis.client.particles.options.SparkleParticleOptions;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.common.status_effects.CStatusEffects;
import net.junebug.chrysalis.util.entities.interfaces.AnimatedEntity;
import net.junebug.chrysalis.util.helpers.ComponentHelper;
import net.junebug.chrysalis.util.helpers.EntityHelper;
import net.junebug.chrysalis.util.helpers.EventHelper;
import net.junebug.chrysalis.util.helpers.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class KeyGolem extends AbstractGolem implements AnimatedEntity {

    /**
     * A golem that can be carried and used to unlock various blocks.
     **/

    // region Initialization

    public KeyGolem(EntityType<? extends KeyGolem> entityType, Level level) {
        super(entityType, level);

        this.setPersistenceRequired();

        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.LAVA, -1.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(PathType.DANGER_POWDER_SNOW, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
        this.setPathfindingMalus(PathType.TRAPDOOR, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.DANGER_OTHER, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_CAUTIOUS, -1.0F);
    }

    public final List<Holder<MobEffect>> defaultEffects = List.of(
        MobEffects.MOVEMENT_SPEED,
        MobEffects.MOVEMENT_SLOWDOWN,
        MobEffects.DIG_SPEED,
        MobEffects.DIG_SLOWDOWN,
        MobEffects.DAMAGE_BOOST,
        MobEffects.JUMP,
        MobEffects.CONFUSION,
        MobEffects.REGENERATION,
        MobEffects.DAMAGE_RESISTANCE,
        MobEffects.FIRE_RESISTANCE,
        MobEffects.WATER_BREATHING,
        MobEffects.INVISIBILITY,
        MobEffects.BLINDNESS,
        MobEffects.NIGHT_VISION,
        MobEffects.HUNGER,
        MobEffects.WEAKNESS,
        MobEffects.HEALTH_BOOST,
        MobEffects.ABSORPTION,
        MobEffects.SATURATION,
        MobEffects.GLOWING,
        MobEffects.LEVITATION,
        MobEffects.SLOW_FALLING,
        MobEffects.DOLPHINS_GRACE,
        MobEffects.DARKNESS,
        MobEffects.WIND_CHARGED,
        MobEffects.WEAVING,
        MobEffects.OOZING,
        MobEffects.INFESTED,
        CStatusEffects.HEALTH_REDUCTION,
        CStatusEffects.BUILDING_FATIGUE
    );

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
    }

    @Override
    protected @NotNull AABB makeBoundingBox(@NotNull Vec3 position) {
        return EntityHelper.setHitboxSize(this, this.getHitboxScale(), 0.9F);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (HITBOX_SCALE.equals(key)) this.refreshDimensions();
        super.onSyncedDataUpdated(key);
    }

    @SuppressWarnings("deprecation")
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor serverLevelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData) {
        this.setVariant(KeyGolemVariant.byRandomIdWithoutEnchanted());
        if (this.getVariant().isEnchanted()) this.setRandomGivenEffect();
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, spawnReason, spawnGroupData);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance mobEffectInstance) {
        return false;
    }

    // endregion

    // region Entity Data

    private static final EntityDataAccessor<Boolean>
        FAKE = SynchedEntityData.defineId(KeyGolem.class, EntityDataSerializers.BOOLEAN),
        PLAYING_NOVELTY_ANIMATION = SynchedEntityData.defineId(KeyGolem.class, EntityDataSerializers.BOOLEAN),
        DESPAWN_TRIGGERED = SynchedEntityData.defineId(KeyGolem.class, EntityDataSerializers.BOOLEAN)
    ;

    private static final EntityDataAccessor<Integer>
        VARIANT = SynchedEntityData.defineId(KeyGolem.class, EntityDataSerializers.INT),
        BRIGHTNESS = SynchedEntityData.defineId(KeyGolem.class, EntityDataSerializers.INT),
        GIVEN_EFFECT_AMPLIFIER = SynchedEntityData.defineId(KeyGolem.class, EntityDataSerializers.INT),
        TICKS_UNTIL_DESPAWN = SynchedEntityData.defineId(KeyGolem.class, EntityDataSerializers.INT)
    ;

    private static final EntityDataAccessor<Float> HITBOX_SCALE = SynchedEntityData.defineId(KeyGolem.class, EntityDataSerializers.FLOAT);

    private final String
        variantTag = "variant",
        brightnessTag = "brightness",
        fakeTag = "fake",
        givenEffectTag = "given_effect",
        givenEffectAmplifierTag = "given_effect_amplifier"
    ;

    private final int defaultTicksUntilDespawn = 10;
    private final float defaultHitboxWidth = 0.6F;

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, KeyGolemVariant.GOLDEN.id());
        builder.define(BRIGHTNESS, 15);
        builder.define(FAKE, false);
        builder.define(GIVEN_EFFECT_AMPLIFIER, 0);
        builder.define(PLAYING_NOVELTY_ANIMATION, false);
        builder.define(DESPAWN_TRIGGERED, false);
        builder.define(TICKS_UNTIL_DESPAWN, this.defaultTicksUntilDespawn);
        builder.define(HITBOX_SCALE, this.defaultHitboxWidth);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt(this.variantTag, this.getVariant().id());
        compoundTag.putInt(this.brightnessTag, this.getBrightness());
        compoundTag.putBoolean(this.fakeTag, this.isFake());
        compoundTag.putInt(this.givenEffectAmplifierTag, this.getGivenEffectAmplifier());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setVariant(KeyGolemVariant.byId(compoundTag.getInt(this.variantTag)));
        this.setBrightness(compoundTag.getInt(this.brightnessTag));
        this.setFake(compoundTag.getBoolean(this.fakeTag));
        this.setGivenEffect(compoundTag, this.getGivenEffect(compoundTag));
        this.setGivenEffectAmplifier(compoundTag.getInt(this.givenEffectAmplifierTag));
    }

    // region In-Game Accessible Data

    public @NotNull KeyGolemVariant getVariant() {
        return KeyGolemVariant.byId(this.getEntityData().get(VARIANT));
    }

    private void setVariant(KeyGolemVariant keyGolemVariant) {
        this.getEntityData().set(VARIANT, keyGolemVariant.id());
    }

    public int getBrightness() {
        return this.getEntityData().get(BRIGHTNESS);
    }

    private void setBrightness(int brightness) {
        this.getEntityData().set(BRIGHTNESS, brightness);
    }

    public boolean isFake() {
        return this.getEntityData().get(FAKE);
    }

    private void setFake(boolean fake) {
        this.getEntityData().set(FAKE, fake);
    }

    // region Given Effect

    private Holder<MobEffect> getGivenEffect(CompoundTag compoundTag) {
        if (compoundTag.contains(this.givenEffectTag, 8)) {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(compoundTag.getString(this.givenEffectTag));
            return resourceLocation == null ? null : BuiltInRegistries.MOB_EFFECT.get(resourceLocation).orElse(null);
        } else {
            return null;
        }
    }

    private void setGivenEffect(CompoundTag compoundTag, @Nullable Holder<MobEffect> mobEffect) {
        if (mobEffect == null) return;
        mobEffect.unwrapKey().ifPresent(resourceKey -> compoundTag.putString(this.givenEffectTag, resourceKey.location().toString()));
    }

    @SuppressWarnings("all")
    private void setGivenEffectWithAmplifier(CompoundTag compoundTag, @Nullable Holder<MobEffect> mobEffect, int amplifier) {
        this.setGivenEffect(compoundTag, mobEffect);
        this.setGivenEffectAmplifier(amplifier);
    }

    private void setRandomGivenEffect() {
        this.setGivenEffectWithAmplifier(this.getPersistentData(), this.defaultEffects.get(this.getRandom().nextInt(this.defaultEffects.size())), 0);
    }

    private int getGivenEffectAmplifier() {
        return this.getEntityData().get(GIVEN_EFFECT_AMPLIFIER);
    }

    private void setGivenEffectAmplifier(int amplifier) {
        this.getEntityData().set(GIVEN_EFFECT_AMPLIFIER, amplifier);
    }

    private MobEffectInstance getGivenEffectInstance(Holder<MobEffect> mobEffect) {
        return new MobEffectInstance(mobEffect, 400, this.getGivenEffectAmplifier(), true, true);
    }

    // endregion

    // endregion

    // region Technical Data

    public boolean isPlayingNoveltyAnimation() {
        return this.getEntityData().get(PLAYING_NOVELTY_ANIMATION);
    }

    private void setPlayingNoveltyAnimation(boolean isPlayingNoveltyAnimation) {
        this.getEntityData().set(PLAYING_NOVELTY_ANIMATION, isPlayingNoveltyAnimation);
    }

    public boolean isDespawnTriggered() {
        return this.getEntityData().get(DESPAWN_TRIGGERED);
    }

    private void setDespawnTriggered() {
        this.getEntityData().set(DESPAWN_TRIGGERED, true);
        this.setTicksUntilDespawn(this.defaultTicksUntilDespawn);
    }

    public int getTicksUntilDespawn() {
        return this.getEntityData().get(TICKS_UNTIL_DESPAWN);
    }

    private void setTicksUntilDespawn(int ticksUntilDespawn) {
        this.getEntityData().set(TICKS_UNTIL_DESPAWN, ticksUntilDespawn);
    }

    private float getHitboxScale() {
        return this.getEntityData().get(HITBOX_SCALE);
    }

    private void setHitboxScale(float hitboxScale) {
        this.getEntityData().set(HITBOX_SCALE, hitboxScale);
    }

    // endregion

    // endregion

    // region Navigation and Riding

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, @NotNull BlockState landedState, @NotNull BlockPos landedPosition) {}

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public boolean startRiding(@NotNull Entity entity, boolean force) {

        if (entity instanceof ServerPlayer serverPlayer) {

            EventHelper.sendSystemMessageWithTwoIcons(serverPlayer, Chrysalis.MOD_ID, ComponentHelper.KEY_GOLEM_ICON, Component.translatable("gui.chrysalis.key_golem.grab_message"), true);
            Holder<MobEffect> mobEffect = this.getGivenEffect(this.getPersistentData());

            if (mobEffect != null) {
                serverPlayer.addEffect(this.getGivenEffectInstance(mobEffect), this);
                this.playEffectChangedSound(CSoundEvents.KEY_GOLEM_ADD_PLAYER_EFFECT.get());
            }
        }

        return super.startRiding(entity, force);
    }

    @Override
    public void stopRiding() {

        if (this.getVehicle() instanceof Player player) {

            this.makeSound(CSoundEvents.KEY_GOLEM_DROP.get());
            player.swing(player.getUsedItemHand());
            if (player instanceof ServerPlayer serverPlayer) EventHelper.sendSystemMessageWithTwoIcons(serverPlayer, Chrysalis.MOD_ID, ComponentHelper.WARNING_ICON, Component.translatable("gui.chrysalis.key_golem.drop_message", this.getName().getString()).withStyle(ChatFormatting.RED), true);

            Holder<MobEffect> mobEffect = this.getGivenEffect(this.getPersistentData());

            if (mobEffect != null && player.hasEffect(mobEffect)) {

                MobEffectInstance mobEffectInstance = player.getEffect(mobEffect);

                if (mobEffectInstance != null && mobEffectInstance.getAmplifier() == this.getGivenEffectAmplifier() && mobEffectInstance.isAmbient()) {
                    player.removeEffect(mobEffect);
                    this.playEffectChangedSound(CSoundEvents.KEY_GOLEM_REMOVE_PLAYER_EFFECT.get());
                }
            }
        }

        super.stopRiding();
    }

    @Override
    public void rideTick() {
        Holder<MobEffect> mobEffect = this.getGivenEffect(this.getPersistentData());
        if (this.getVehicle() instanceof Player player && mobEffect != null && this.tickCount % 20 == 0) player.addEffect(this.getGivenEffectInstance(mobEffect), this);
        super.rideTick();
    }

    public boolean isRidingPlayer() {
        return this.getVehicle() instanceof Player;
    }

    public boolean isRidingSpecificPlayer(Player player) {
        return this.isRidingPlayer() && this.getVehicle() == player;
    }

    public static KeyGolem getHeldKeyGolem(Player player) {
        return !player.getPassengers().isEmpty() && player.getPassengers().getFirst() instanceof KeyGolem keyGolem ? keyGolem : null;
    }

    // endregion

    // region Ticking, AI, and Entity Events

    public final AnimationState
        idleAnimationState = new AnimationState(),
        noveltyAnimationState = new AnimationState(),
        fallAsleepAnimationState = new AnimationState(),
        sleepAnimationState = new AnimationState(),
        wakeUpAnimationState = new AnimationState(),
        noticeAnimationState = new AnimationState(),
        carryAnimationState = new AnimationState(),
        spawnAnimationState = new AnimationState(),
        despawnAnimationState = new AnimationState()
    ;

    @Override
    public AnimationState getIdleAnimation() {
        return this.idleAnimationState;
    }

    @Override
    public AnimationState getNoveltyAnimation() {
        return this.noveltyAnimationState;
    }

    @Override
    public boolean canPlayNoveltyAnimation(LivingEntity livingEntity) {
        return AnimatedEntity.super.canPlayNoveltyAnimation(livingEntity) && !this.isPlayingNoveltyAnimation() && !this.isRidingPlayer() && !EntityHelper.isMoving(this);
    }

    @Override
    public AnimationState getSpawnAnimation() {
        return this.spawnAnimationState;
    }

    @Override
    public AnimationState getDespawnAnimation() {
        return this.despawnAnimationState;
    }

    @Override
    public void handleEntityEvent(byte state) {
        if (state == 70) this.playNoveltyAnimation(this); // Novelty Animation
        else if (state == 71) this.playSpawnAnimation(this); // Spawn Animation
        else if (state == 72) this.playDespawnAnimation(this); // Despawn Animation
        else super.handleEntityEvent(state);
    }

    private float noveltyAnimationTicks = 0;

    @Override
    public void tick() {

        if (this.level().isClientSide()) {

            this.getIdleAnimation().startIfStopped(this.tickCount);
            this.carryAnimationState.animateWhen(this.isRidingPlayer(), this.tickCount);
            if (this.carryAnimationState.isStarted()) this.getNoveltyAnimation().stop();
            this.tryStoppingNoveltyAnimation(this, 2750L);

        } else {

            if (this.firstTick) {
                this.setHitboxScale(this.defaultHitboxWidth);
                if (this.getVariant().isEnchanted() && this.getGivenEffect(this.getPersistentData()) == null) this.setRandomGivenEffect();
            }

            if (this.isRidingPlayer() && this.tickCount % 48 == 0) this.playSound(CSoundEvents.KEY_GOLEM_PANT.get(), 1.0F, this.getRandom().triangle(this.getVariant().isEnchanted() ? 0.8F : 1.0F, 0.2F));
            if (this.tryPlayingNoveltyAnimation(this, 70, CSoundEvents.KEY_GOLEM_NOVELTY.get(), 1000)) this.setPlayingNoveltyAnimation(true);

            if (this.isPlayingNoveltyAnimation() && this.noveltyAnimationTicks < 55) {
                ++this.noveltyAnimationTicks;
            } else {
                this.setPlayingNoveltyAnimation(false);
                this.noveltyAnimationTicks = 0;
            }

            if (this.isDespawnTriggered()) {
                this.setTicksUntilDespawn(this.getTicksUntilDespawn() - 1);
                if (this.getHitboxScale() > 0.05F) this.setHitboxScale(this.getHitboxScale() - 0.15F);
            }

            if (this.getTicksUntilDespawn() <= 0) this.remove(RemovalReason.DISCARDED);

        }

        super.tick();
    }

    @Override
    public void aiStep() {

        if (!this.level().isClientSide()) {
            if (EntityHelper.isLivingEntityMoving(this) || this.getVehicle() instanceof Player player && EntityHelper.isLivingEntityMoving(player)) ParticleHelper.emitParticlesAroundEntity(this, new RotatingDustParticleOptions(this.getVariant().particleColor(), false, true, true, 1.0F), 0.5D, 1);
            else if (this.getVariant().isEnchanted() && this.tickCount % 10 == 0) ParticleHelper.emitParticlesAroundEntity(this, new SparkleParticleOptions(this.getVariant().particleColor(), false), 0.8D, 1);
        }

        super.aiStep();
    }

    // endregion

    // region Interactions

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float damageAmount) {

        if (this.isInvulnerableTo(serverLevel, damageSource)) {
            return false;
        } else {

            if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damageSource.isCreativePlayer()) {
                if (damageSource.getEntity() instanceof Player player && damageSource.is(DamageTypeTags.IS_PLAYER_ATTACK)) this.tryRidingPlayer(player);
                return false;
            }

            return super.hurtServer(serverLevel, damageSource, damageAmount);
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {

        PotionContents potionContents = player.getMainHandItem().get(DataComponents.POTION_CONTENTS);

        if (this.getVariant().isEnchanted() && player.canUseGameMasterBlocks() && potionContents != null && potionContents.hasEffects()) {

            Holder<MobEffect> mobEffect = potionContents.getAllEffects().iterator().next().getEffect();
            int amplifier = potionContents.getAllEffects().iterator().next().getAmplifier();

            this.playSound(CSoundEvents.KEY_GOLEM_APPLY_EFFECT.get());
            this.gameEvent(GameEvent.ENTITY_INTERACT);
            ParticleHelper.emitParticlesAroundEntity(this, ParticleTypes.EFFECT, 0.5D, 10);
            this.setGivenEffectWithAmplifier(this.getPersistentData(), mobEffect, amplifier);
            player.awardStat(Stats.ITEM_USED.get(player.getMainHandItem().getItem()));

            if (player instanceof ServerPlayer serverPlayer) {
                Component component = amplifier > 0 ? Component.translatable("gui.chrysalis.key_golem.apply_stronger_effect_message", mobEffect.value().getDisplayName(), Component.translatable("potion.potency." + amplifier)) : Component.translatable("gui.chrysalis.key_golem.apply_effect_message", mobEffect.value().getDisplayName());
                EventHelper.sendSystemMessageWithTwoIcons(serverPlayer, Chrysalis.MOD_ID, ComponentHelper.POTION_ICON, component, true);
            }

            return InteractionResult.SUCCESS_SERVER;
        }

        if (player.getMainHandItem().isEmpty() && this.tryRidingPlayer(player)) return InteractionResult.SUCCESS_SERVER;
        return super.mobInteract(player, interactionHand);
    }

    private boolean canRidePlayer(Player player) {
        return !this.isRemoved() && !this.isDeadOrDying() && !this.isRidingPlayer() && !player.isCrouching() && player.hurtTime <= 0;
    }

    private boolean tryRidingPlayer(Player player) {

        if (!this.canRidePlayer(player)) return false;
        this.gameEvent(GameEvent.ENTITY_INTERACT);

        if (this.isFake()) {
            this.despawnFakeKeyGolem();
        } else {
            this.makeSound(CSoundEvents.KEY_GOLEM_GRAB.get());
            ParticleHelper.emitParticlesAroundEntity(this, new SparkParticleOptions(this.getVariant().particleColor(), false, 1.5F), 0.5D, 5);
            player.ejectPassengers();
            this.startRiding(player, true);
        }

        return true;
    }

    public void despawnFakeKeyGolem() {
        this.makeSound(CSoundEvents.KEY_GOLEM_DISAPPEAR.get());
        ParticleHelper.emitParticlesAroundEntity(this, new DustCloudParticleOptions(this.getVariant().particleColor(), false, true), 1.0D, 5);
        this.level().broadcastEntityEvent(this, (byte) 72);
        this.setDespawnTriggered();
    }

    // endregion

    // region Sounds

    @Override @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isRidingPlayer()) return CSoundEvents.KEY_GOLEM_AMBIENT_CARRYING.get();
        else return CSoundEvents.KEY_GOLEM_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        if (this.isRidingPlayer()) return 20;
        return 80;
    }

    @Override
    public float getVoicePitch() {
        if (this.getVariant().isEnchanted()) return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.8F;
        return super.getVoicePitch();
    }

    @Override @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return CSoundEvents.KEY_GOLEM_HURT.get();
    }

    @Override @Nullable
    protected SoundEvent getDeathSound() {
        return CSoundEvents.KEY_GOLEM_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        this.makeSound(CSoundEvents.KEY_GOLEM_STEP.get());
    }

    public void playRattleSound(KeyGolem keyGolem) {
        keyGolem.makeSound(CSoundEvents.KEY_GOLEM_RATTLE.get());
        keyGolem.gameEvent(GameEvent.ENTITY_ACTION);
    }

    private void playEffectChangedSound(SoundEvent soundEvent) {
        this.playSound(soundEvent);
        this.gameEvent(GameEvent.ENTITY_ACTION);
    }

    // endregion
}