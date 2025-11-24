package net.junebug.chrysalis.mixin.entities.misc;

import net.junebug.chrysalis.client.particles.options.LargePulsationParticleOptions;
import net.junebug.chrysalis.client.particles.options.SmallPulsationParticleOptions;
import net.junebug.chrysalis.common.blocks.custom_blocks.BarricadeFullBlock;
import net.junebug.chrysalis.common.blocks.custom_blocks.BarricadeMultifaceBlock;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.util.helpers.BlockHelper;
import net.junebug.chrysalis.util.helpers.ComponentHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.awt.*;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends Entity {

    @Unique private Projectile chrysalis$projectile = (Projectile) (Object) this;

    private ProjectileMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Deflects all projectiles that hit the world border or barricades.
     **/

    @Inject(method = "hitTargetOrDeflectSelf", at = @At("HEAD"), cancellable = true)
    private void chrysalis$deflectProjectilesFromWorldBorderAndBarricades(HitResult hitResult, CallbackInfoReturnable<ProjectileDeflection> cir) {

        if (!(hitResult instanceof BlockHitResult blockHitResult)) return;
        BlockState blockState = this.level().getBlockState(blockHitResult.getBlockPos());

        if (blockHitResult.isWorldBorderHit()) {

            this.chrysalis$deflectProjectile(blockHitResult, blockHitResult.getDirection().getOpposite(), ComponentHelper.BARRICADE_COLOR.getRGB(), CSoundEvents.WORLD_BORDER_DEFLECT.get());
            cir.setReturnValue(ProjectileDeflection.REVERSE);

        } else if (this.getDeltaMovement().length() >= 0.25D && blockState.is(CTags.BARRICADES)) {

            int particleColor;

            if (blockState.getBlock() instanceof BarricadeMultifaceBlock barricadeMultifaceBlock) particleColor = barricadeMultifaceBlock.particleColor;
            else if (blockState.getBlock() instanceof BarricadeFullBlock barricadeFullBlock) particleColor = barricadeFullBlock.particleColor;
            else particleColor = Color.LIGHT_GRAY.getRGB();

            this.chrysalis$deflectProjectile(blockHitResult, blockHitResult.getDirection(), particleColor, CSoundEvents.BARRICADE_DEFLECT.get());
            cir.setReturnValue(ProjectileDeflection.REVERSE);
        }
    }

    @Unique
    private void chrysalis$deflectProjectile(BlockHitResult blockHitResult, Direction direction, int particleColor, SoundEvent soundEvent) {
        ParticleOptions particleOptions = this.getDeltaMovement().length() >= 2.0D ? new LargePulsationParticleOptions(particleColor, false, direction.get3DDataValue(), 10) : new SmallPulsationParticleOptions(particleColor, false, direction.get3DDataValue(), 10);
        if (this.level() instanceof ServerLevel serverLevel) serverLevel.sendParticles(particleOptions, this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
        BlockHelper.deflectProjectileWithSound(this.chrysalis$projectile, 0.2D, blockHitResult, soundEvent, this.getRandom().nextFloat() * 0.4F + 0.8F);
    }

    @SuppressWarnings("unused")
    @Mixin(WindCharge.class)
    public static abstract class WindChargeMixin extends AbstractWindCharge {

        private WindChargeMixin(EntityType<? extends AbstractWindCharge> entityType, Level level) {
            super(entityType, level);
        }

        /**
         * Allows for wind charges to always be deflected off of the world border and barricades regardless of their noDeflectTicks value.
         **/

        @Inject(method = "deflect", at = @At("HEAD"), cancellable = true)
        private void chrysalis$allowWindChargeDeflectionFromBarricades(ProjectileDeflection projectileDeflection, Entity entity, Entity owner, boolean deflectedByPlayer, CallbackInfoReturnable<Boolean> cir) {
            if (ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity, this.getClipType()) instanceof BlockHitResult blockHitResult && (blockHitResult.isWorldBorderHit() || this.level().getBlockState(blockHitResult.getBlockPos()).is(CTags.BARRICADES))) cir.setReturnValue(super.deflect(projectileDeflection, entity, owner, deflectedByPlayer));
        }
    }
}