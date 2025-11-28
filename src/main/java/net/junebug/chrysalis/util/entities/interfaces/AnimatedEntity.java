package net.junebug.chrysalis.util.entities.interfaces;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;

@SuppressWarnings("unused")
public interface AnimatedEntity {

    /**
     * An interface for entities with custom keyframe animations.
     **/

    // region Spawning Animation

    default AnimationState getSpawningAnimation() {
        return new AnimationState();
    }

    default void playSpawningAnimation(Entity entity) {
        this.getSpawningAnimation().start(entity.tickCount);
    }

    // endregion

    // region Despawning Animation

    default AnimationState getDespawningAnimation() {
        return new AnimationState();
    }

    default void playDespawningAnimation(Entity entity) {
        this.getDespawningAnimation().start(entity.tickCount);
    }

    // endregion

    // region Idle Animation

    default AnimationState getIdleAnimation() {
        return new AnimationState();
    }

    default void playIdleAnimation(Entity entity, boolean animateWhen) {
        this.getIdleAnimation().animateWhen(animateWhen, entity.tickCount);
    }

    // endregion

    // region Novelty Animation

    default AnimationState getNoveltyAnimation() {
        return new AnimationState();
    }

    default void playNoveltyAnimation(Entity entity) {
        this.getNoveltyAnimation().start(entity.tickCount);
    }

    default void tryPlayingNoveltyAnimation(Entity entity, int entityEvent, SoundEvent soundEvent, int chance, boolean canPlay) {
        if (entity.level().getRandom().nextInt(chance) == 0 && canPlay) {
            entity.level().broadcastEntityEvent(entity, (byte) entityEvent);
            entity.playSound(soundEvent);
            entity.gameEvent(GameEvent.ENTITY_ACTION);
        }
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

    // region Attacking Animations

    default AnimationState getAttackingAnimation() {
        return new AnimationState();
    }

    default void playAttackingAnimation(Entity entity) {
        this.getAttackingAnimation().start(entity.tickCount);
    }

    default void playAlternatingAttackAnimation(Entity entity) {
        if (entity.level().getRandom().nextFloat() < 0.5F) this.playRightAttackingAnimation(entity);
        else this.playLeftAttackingAnimation(entity);
    }

    default AnimationState getRightAttackingAnimation() {
        return new AnimationState();
    }

    default void playRightAttackingAnimation(Entity entity) {
        this.getRightAttackingAnimation().start(entity.tickCount);
    }

    default AnimationState getLeftAttackingAnimation() {
        return new AnimationState();
    }

    default void playLeftAttackingAnimation(Entity entity) {
        this.getLeftAttackingAnimation().start(entity.tickCount);
    }

    // endregion
}