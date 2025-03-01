package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameRules;
import net.sydokiddo.chrysalis.util.entities.EncounterMusicMob;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    private MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique private Mob chrysalis$mob = (Mob) (Object) this;
    @Shadow @Nullable public abstract LivingEntity getTarget();

    /**
     * If a mob is an instance of the EncounterMusicMob interface, it will play encounter music upon aggravation.
     **/

    @Inject(method = "setTarget", at = @At("TAIL"))
    private void chrysalis$startEncounterMusic(LivingEntity livingEntity, CallbackInfo info) {
        if (this.getTarget() != null) this.chrysalis$tryToSendEncounterMusic(true);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void chrysalis$refreshEncounterMusic(CallbackInfo info) {
        if (!this.level().isClientSide() && this.getType().is(ChrysalisTags.ALWAYS_PLAYS_ENCOUNTER_MUSIC) && this.tickCount % 20 == 0) this.chrysalis$tryToSendEncounterMusic(true);
        this.chrysalis$tryToSendEncounterMusic(false);
    }

    @Unique
    private void chrysalis$tryToSendEncounterMusic(boolean playOnFirstTick) {
        if (!this.level().isClientSide() && this.chrysalis$mob instanceof EncounterMusicMob encounterMusicMob &&
        (playOnFirstTick ? encounterMusicMob.chrysalis$shouldStartEncounterMusic() : encounterMusicMob.chrysalis$shouldRefreshEncounterMusic())) encounterMusicMob.chrysalis$sendEncounterMusic(this.chrysalis$mob, playOnFirstTick);
    }

    /**
     * Mobs being able to pick up items is now determined by the mobWorldInteractions game rule rather than the mobGriefing game rule.
     **/

    @ModifyArg(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$mobsPickingUpItemsWorldInteractionsGameRule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS;
    }

    /**
     * Dealing more than a mob's damage cap will set the damage back to said damage cap.
     **/

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float damageAmount) {
        return super.hurtServer(serverLevel, damageSource, EntityDataHelper.getDamageCap(this, damageSource, damageAmount));
    }
}