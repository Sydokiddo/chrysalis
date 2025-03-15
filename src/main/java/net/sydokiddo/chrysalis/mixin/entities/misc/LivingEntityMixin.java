package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.common.misc.ChrysalisAttributes;
import net.sydokiddo.chrysalis.common.misc.ChrysalisDamageTypes;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.MobSightEffect;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Collection;
import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Unique LivingEntity chrysalis$livingEntity = (LivingEntity) (Object) this;
    @Shadow protected abstract void dropEquipment(ServerLevel serverLevel);
    @Shadow public abstract double getAttributeValue(Holder<Attribute> holder);
    @Shadow public abstract boolean hasEffect(Holder<MobEffect> holder);
    @Shadow public abstract @Nullable MobEffectInstance getEffect(Holder<MobEffect> holder);
    @Shadow public abstract Collection<MobEffectInstance> getActiveEffects();

    private LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Adds Chrysalis's new attributes to living entities.
     **/

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void chrysalis$addLivingEntityAttributes(final CallbackInfoReturnable<AttributeSupplier.Builder> info) {
        if (info.getReturnValue() != null) {
            info.getReturnValue().add(Holder.direct(ChrysalisAttributes.REDUCED_DETECTION_RANGE.get()));
            info.getReturnValue().add(Holder.direct(ChrysalisAttributes.DAMAGE_CAPACITY.get()));
        }
    }

    /**
     * Prevents non-player living entities from dropping loot when killed with a kill wand.
     **/

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventLootDropping(ServerLevel serverLevel, DamageSource damageSource, CallbackInfo info) {
        if (damageSource.is(ChrysalisDamageTypes.KILL_WAND) && !(this.chrysalis$livingEntity instanceof Player)) {
            info.cancel();
            this.dropEquipment(serverLevel);
        }
    }

    /**
     * Mobs are now affected by the blindness status effect, which decreases their visibility percentage depending on the amplifier of the effect.
     * <p>
     * Additionally, the reduced detection range attribute can configure the range at which mobs can see the entity with it.
     **/

    @Inject(method = "getVisibilityPercent", at = @At(value = "RETURN"), cancellable = true)
    private void chrysalis$changeMobVisibilityPercentage(Entity entity, CallbackInfoReturnable<Double> cir) {

        if (this.level().isClientSide() || !(entity instanceof LivingEntity self)) return;
        Holder<MobEffect> blindness = MobEffects.BLINDNESS;

        if (self.hasEffect(blindness)) {
            cir.setReturnValue(cir.getReturnValue() / (2.0D * (Objects.requireNonNull(self.getEffect(blindness)).getAmplifier() + 1)));
        } else {
            double reducedDetectionRangeAttribute = this.getAttributeValue(Holder.direct(ChrysalisAttributes.REDUCED_DETECTION_RANGE.get()));
            if (reducedDetectionRangeAttribute > 0.0D) cir.setReturnValue(cir.getReturnValue() * (1.0D - reducedDetectionRangeAttribute));
        }
    }

    /**
     * Entities with invisibility 2 or higher will not cause mobs to detect their armor.
     **/

    @Inject(method = "getArmorCoverPercentage", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$changeArmorCoverPercentage(CallbackInfoReturnable<Float> cir) {
        if (this.level().isClientSide()) return;
        Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
        if (this.hasEffect(invisibility) && Objects.requireNonNull(this.getEffect(invisibility)).getAmplifier() > 0) cir.setReturnValue(0.0F);
    }

    /**
     * Refreshes any mob sight rendering effects when the effect wears out.
     **/

    @Inject(method = "tickEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateGlowingStatus()V"))
    private void chrysalis$updateMobSightStatuses(CallbackInfo info) {
        if (this.getActiveEffects().stream().noneMatch(mobEffectInstance -> mobEffectInstance.getEffect() instanceof MobSightEffect)) MobSightEffect.tryRefreshingPostEffect(this.chrysalis$livingEntity);
    }
}