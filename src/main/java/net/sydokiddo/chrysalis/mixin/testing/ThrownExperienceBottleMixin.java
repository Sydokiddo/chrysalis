package net.sydokiddo.chrysalis.mixin.testing;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.sydokiddo.chrysalis.client.particles.options.SparkParticleOptions;
import net.sydokiddo.chrysalis.util.helpers.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.awt.*;

@Mixin(ThrownExperienceBottle.class)
public abstract class ThrownExperienceBottleMixin extends ThrowableItemProjectile {

    public ThrownExperienceBottleMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void chrysalis$changeExperienceBottleOnHitBehavior(HitResult hitResult, CallbackInfo info) {

        info.cancel();
        super.onHit(hitResult);

        if (this.level() instanceof ServerLevel serverLevel) {

            this.playSound(SoundEvents.SPLASH_POTION_BREAK, 1.0F, this.getRandom().nextFloat() * 0.1F + 0.9F);

            for (int particleAmount = 0; particleAmount < 12; ++particleAmount) {
                serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EXPERIENCE_BOTTLE)), this.getRandomX(1.0D), this.getY(1.2D), this.getRandomZ(1.0D), 1, 0.0D, 0.0D, 0.0D, 0.2D);
                serverLevel.sendParticles(new SparkParticleOptions(Color.decode("#B9E85C").getRGB(), true, MathHelper.getFloatBetween(serverLevel.getRandom(), 1.0F, 4.0F)), this.getRandomX(1.0D), this.getY(1.2D), this.getRandomZ(1.0D), 1, 0.0D, 0.0D, 0.0D, 0.2D);
            }

            ExperienceOrb.award(serverLevel, this.position(), 6 + this.level().getRandom().nextInt(10) + this.level().getRandom().nextInt(10));
            this.discard();
        }
    }
}