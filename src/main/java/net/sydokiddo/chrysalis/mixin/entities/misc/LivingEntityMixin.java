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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisAttributes;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisDamageSources;
import net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects.MobSightEffect;
import net.sydokiddo.chrysalis.util.entities.codecs.EntityDetectionRangeData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Unique LivingEntity livingEntity = (LivingEntity) (Object) this;
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);
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
            info.getReturnValue().add(ChrysalisAttributes.REDUCED_DETECTION_RANGE);
            info.getReturnValue().add(ChrysalisAttributes.DAMAGE_CAPACITY);
        }
    }

    /**
     * Prevents non-player living entities from dropping loot when killed with a kill wand.
     **/

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventLootDropping(ServerLevel serverLevel, DamageSource damageSource, CallbackInfo info) {
        if (damageSource.is(ChrysalisDamageSources.KILL_WAND) && !(this.livingEntity instanceof Player)) {
            info.cancel();
            this.dropEquipment(serverLevel);
        }
    }

    // region Reworked Mob Visibility

    /**
     * Mobs are now affected by the blindness status effect, which decreases their visibility percentage depending on the amplifier of the effect.
     * <p>
     * Additionally, mobs can have reduced visibility of any entity wearing a specific item in a specified slot, registered through a data pack.
     **/

    @Inject(method = "getVisibilityPercent", at = @At(value = "RETURN"), cancellable = true)
    private void chrysalis$changeMobVisibilityPercentage(Entity entity, CallbackInfoReturnable<Double> cir) {

        if (this.level().isClientSide() || !(entity instanceof LivingEntity self)) return;
        Holder<MobEffect> blindness = MobEffects.BLINDNESS;

        if (self.hasEffect(blindness)) {
            cir.setReturnValue(cir.getReturnValue() / (2.0D * (Objects.requireNonNull(self.getEffect(blindness)).getAmplifier() + 1)));
        } else {

            if (Chrysalis.registryAccess == null) return;
            Optional<EntityDetectionRangeData> optional = Chrysalis.registryAccess.lookupOrThrow(ChrysalisRegistry.ENTITY_DETECTION_RANGE_DATA).stream().findFirst();

            if (optional.isEmpty() || optional.get().forTesting() && !Chrysalis.IS_DEBUG) {
                double reducedDetectionRangeAttribute = this.getAttributeValue(ChrysalisAttributes.REDUCED_DETECTION_RANGE);
                if (reducedDetectionRangeAttribute > 0.0D) cir.setReturnValue(cir.getReturnValue() * (1.0D - reducedDetectionRangeAttribute));
            } else if (self.getType().is(optional.get().entities()) && this.getItemBySlot(EquipmentSlot.byName(optional.get().equipmentSlot())).is(optional.get().items())) {
                cir.setReturnValue(cir.getReturnValue() * optional.get().detectionPercentage());
            }
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

    // endregion

    /**
     * Refreshes any mob sight rendering effects when the effect wears out.
     **/

    @Inject(method = "tickEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateGlowingStatus()V"))
    private void chrysalis$updateMobSightStatuses(CallbackInfo info) {
        if (this.getActiveEffects().stream().noneMatch(mobEffectInstance -> mobEffectInstance.getEffect() instanceof MobSightEffect)) MobSightEffect.tryRefreshingPostEffect(this.livingEntity);
    }
}