package net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem;

import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.util.entities.interfaces.AnimatedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KeyGolem extends AbstractGolem implements AnimatedEntity {

    /**
     * A golem that can be carried and used to unlock various blocks.
     **/

    // region Initialization

    public KeyGolem(EntityType<? extends KeyGolem> entityType, Level level) {
        super(entityType, level);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 1.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
    }

    // endregion

    // region Navigation and Riding

    @Override
    public boolean onClimbable() {
        return false;
    }

    public boolean isRidingPlayer() {
        return this.getVehicle() instanceof Player;
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
        carryAnimationState = new AnimationState()
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
    public void handleEntityEvent(byte state) {
        if (state == 70) this.playNoveltyAnimation(this); // Novelty Animation
        else super.handleEntityEvent(state);
    }

    @Override
    public void tick() {

        if (this.level().isClientSide()) {

            this.getIdleAnimation().startIfStopped(this.tickCount);
            this.carryAnimationState.animateWhen(this.isRidingPlayer(), this.tickCount);

        } else {
            this.tryPlayingNoveltyAnimation(this, 70, CSoundEvents.KEY_GOLEM_NOVELTY.get(), 1000, !this.getNoveltyAnimation().isStarted() && !this.isDeadOrDying() && !this.isRidingPlayer());
        }

        super.tick();
    }

    @Override
    protected void checkFallDamage(double heightDifference, boolean onGround, @NotNull BlockState landedState, @NotNull BlockPos landedPosition) {}

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

            if (!damageSource.isCreativePlayer()) {

                if (damageSource.getEntity() instanceof Player player && !player.isCrouching() && player.hurtTime <= 0 && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !this.isRidingPlayer()) {
                    this.playSound(CSoundEvents.KEY_GOLEM_GRAB.get());
                    this.gameEvent(GameEvent.ENTITY_INTERACT);
                    player.ejectPassengers();
                    this.startRiding(player, true);
                }

                return false;
            }
        }

        return super.hurtServer(serverLevel, damageSource, damageAmount);
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
        if (this.isRidingPlayer()) return 25;
        return 80;
    }

    @Override @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override @Nullable
    protected SoundEvent getDeathSound() {
        return CSoundEvents.KEY_GOLEM_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        this.playSound(CSoundEvents.KEY_GOLEM_STEP.get());
    }

    // endregion
}