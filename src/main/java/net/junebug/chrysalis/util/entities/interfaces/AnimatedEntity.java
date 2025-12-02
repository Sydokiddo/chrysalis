package net.junebug.chrysalis.util.entities.interfaces;

import net.junebug.chrysalis.util.helpers.EntityHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("unused")
public interface AnimatedEntity {

    /**
     * An interface for entities with custom keyframe animations.
     **/

    // region Spawn Animation

    default AnimationState getSpawnAnimation() {
        return new AnimationState();
    }

    default void playSpawnAnimation(LivingEntity livingEntity) {
        this.getSpawnAnimation().start(livingEntity.tickCount);
    }

    // endregion

    // region Despawn Animation

    default AnimationState getDespawnAnimation() {
        return new AnimationState();
    }

    default void playDespawnAnimation(LivingEntity livingEntity) {
        this.getDespawnAnimation().start(livingEntity.tickCount);
    }

    // endregion

    // region Idle Animation

    default AnimationState getIdleAnimation() {
        return new AnimationState();
    }

    default void playIdleAnimation(LivingEntity livingEntity, boolean animateWhen) {
        this.getIdleAnimation().animateWhen(animateWhen, livingEntity.tickCount);
    }

    // endregion

    // region Novelty Animation

    default AnimationState getNoveltyAnimation() {
        return new AnimationState();
    }

    default void playNoveltyAnimation(LivingEntity livingEntity) {
        this.getNoveltyAnimation().start(livingEntity.tickCount);
    }

    default boolean canPlayNoveltyAnimation(LivingEntity livingEntity) {
        return !this.getNoveltyAnimation().isStarted() && !livingEntity.isRemoved() && !livingEntity.isDeadOrDying() && livingEntity.isEffectiveAi();
    }

    default boolean playNoveltyAnimation(LivingEntity livingEntity, int entityEvent, SoundEvent soundEvent, int chance) {
        boolean success = this.tryPlayingNoveltyAnimation(livingEntity, entityEvent, chance);
        if (success) EntityHelper.playActionSound(livingEntity, soundEvent);
        return success;
    }

    default boolean playNoveltyAnimationWithCustomPitch(LivingEntity livingEntity, int entityEvent, SoundEvent soundEvent, float soundPitch, int chance) {
        boolean success = this.tryPlayingNoveltyAnimation(livingEntity, entityEvent, chance);
        if (success) EntityHelper.playActionSoundWithCustomPitch(livingEntity, soundEvent, soundPitch);
        return success;
    }

    private boolean tryPlayingNoveltyAnimation(LivingEntity livingEntity, int entityEvent, int chance) {

        if (livingEntity.level().getRandom().nextInt(chance) == 0 && this.canPlayNoveltyAnimation(livingEntity)) {
            livingEntity.level().broadcastEntityEvent(livingEntity, (byte) entityEvent);
            return true;
        }

        return false;
    }

    default void tryStoppingNoveltyAnimation(LivingEntity livingEntity, long animationTime) {
        if (this.getNoveltyAnimation().getTimeInMillis(livingEntity.tickCount) > animationTime) this.getNoveltyAnimation().stop();
    }

    // endregion

    // region Aggravate Animation

    default AnimationState getAggravateAnimation() {
        return new AnimationState();
    }

    default void playAggravateAnimation(Entity entity) {
        this.getAggravateAnimation().start(entity.tickCount);
    }

    // endregion

    // region Attack Animations

    default AnimationState getAttackAnimation() {
        return new AnimationState();
    }

    default void playAttackAnimation(Entity entity) {
        this.getAttackAnimation().start(entity.tickCount);
    }

    default void playAlternatingAttackAnimation(Entity entity) {
        if (entity.level().getRandom().nextFloat() < 0.5F) this.playRightAttackAnimation(entity);
        else this.playLeftAttackAnimation(entity);
    }

    default AnimationState getRightAttackAnimation() {
        return new AnimationState();
    }

    default void playRightAttackAnimation(Entity entity) {
        this.getRightAttackAnimation().start(entity.tickCount);
    }

    default AnimationState getLeftAttackAnimation() {
        return new AnimationState();
    }

    default void playLeftAttackAnimation(Entity entity) {
        this.getLeftAttackAnimation().start(entity.tickCount);
    }

    // endregion
}