package net.junebug.chrysalis.mixin.entities.misc;

import net.junebug.chrysalis.client.particles.options.LargePulsationParticleOptions;
import net.junebug.chrysalis.client.particles.options.SmallPulsationParticleOptions;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.util.helpers.BlockHelper;
import net.junebug.chrysalis.util.helpers.ComponentHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity {

    @Unique private Projectile chrysalis$projectile = (Projectile) (Object) this;

    private ProjectileMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Deflects all projectiles that hit barricade blocks.
     **/

    @Inject(method = "hitTargetOrDeflectSelf", at = @At("HEAD"), cancellable = true)
    private void chrysalis$deflectProjectilesFromBarricades(HitResult hitResult, CallbackInfoReturnable<ProjectileDeflection> cir) {
        if (this.getDeltaMovement().length() >= 0.25D && hitResult instanceof BlockHitResult blockHitResult && this.level().getBlockState(blockHitResult.getBlockPos()).is(CTags.BARRICADES)) {
            ParticleOptions particleOptions = this.getDeltaMovement().length() >= 2.0D ? new LargePulsationParticleOptions(ComponentHelper.BARRICADE_COLOR.getRGB(), false, blockHitResult.getDirection().get3DDataValue(), 10) : new SmallPulsationParticleOptions(ComponentHelper.BARRICADE_COLOR.getRGB(), false, blockHitResult.getDirection().get3DDataValue(), 10);
            if (this.level() instanceof ServerLevel serverLevel) serverLevel.sendParticles(particleOptions, this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            BlockHelper.deflectProjectileWithSound(this.chrysalis$projectile, 0.2D, blockHitResult, CSoundEvents.BARRICADE_DEFLECT.get(), this.getRandom().nextFloat() * 0.4F + 0.8F);
            cir.setReturnValue(ProjectileDeflection.REVERSE);
        }
    }
}