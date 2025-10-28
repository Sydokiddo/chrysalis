package net.junebug.chrysalis.mixin.entities.passive;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.level.Level;
import net.junebug.chrysalis.common.misc.CDamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Squid.class)
public abstract class SquidMixin extends AgeableWaterCreature {

    @Shadow protected abstract void spawnInk();

    private SquidMixin(EntityType<? extends AgeableWaterCreature> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Squids aren't affected by kill wands normally for whatever reason, so this fixes that by skipping the getLastHurtByMob method if the damage source is the kill wand's damage source.
     **/

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    private void chrysalis$makeSquidsAffectedByKillWands(ServerLevel serverLevel, DamageSource damageSource, float damageAmount, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.is(CDamageTypes.KILL_WAND)) {
            super.hurtServer(serverLevel, damageSource, damageAmount);
            this.spawnInk();
            cir.setReturnValue(true);
        }
    }
}