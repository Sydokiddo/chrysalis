package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.core.Holder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisAttributes;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);
    @Shadow public abstract double getAttributeValue(Holder<Attribute> holder);
    @Shadow public abstract boolean hasEffect(Holder<MobEffect> holder);
    @Shadow public abstract @Nullable MobEffectInstance getEffect(Holder<MobEffect> holder);

    private LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void chrysalis$addLivingEntityAttributes(final CallbackInfoReturnable<AttributeSupplier.Builder> info) {
        if (info.getReturnValue() != null) info.getReturnValue().add(ChrysalisAttributes.GENERIC_REDUCED_DETECTION_RANGE);
    }

    // region Reworked Mob Visibility

    /**
     * Mobs are now affected by the Blindness status effect, which decreases their visibility percentage depending on the amplifier of the effect.
     * <p>
     * Additionally, Ender mobs now have reduced visibility of entities wearing anything in their helmet slot that is in the protects_against_endermen tag.
     **/

    @Inject(method = "getVisibilityPercent", at = @At(value = "RETURN"), cancellable = true)
    private void chrysalis$changeMobVisibilityPercentage(Entity entity, CallbackInfoReturnable<Double> cir) {

        if (this.level().isClientSide() || !(entity instanceof LivingEntity livingEntity)) return;
        Holder<MobEffect> blindness = MobEffects.BLINDNESS;

        if (livingEntity.hasEffect(blindness)) {
            cir.setReturnValue(cir.getReturnValue() / (2.0D * (Objects.requireNonNull(livingEntity.getEffect(blindness)).getAmplifier() + 1)));
        } else {
            if (livingEntity.getType().is(ChrysalisTags.ENDER) && this.getItemBySlot(EquipmentSlot.HEAD).is(ItemTags.GAZE_DISGUISE_EQUIPMENT)) {
                cir.setReturnValue(cir.getReturnValue() * 0.5D);
            } else {
                double reducedDetectionRangeAttribute = this.getAttributeValue(ChrysalisAttributes.GENERIC_REDUCED_DETECTION_RANGE);
                if (reducedDetectionRangeAttribute > 0.0D) cir.setReturnValue(cir.getReturnValue() * (1.0D - reducedDetectionRangeAttribute));
            }
        }
    }

    /**
     * Entities with Invisibility 2 or higher will not cause mobs to detect their armor.
     **/

    @Inject(method = "getArmorCoverPercentage", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$changeArmorCoverPercentage(CallbackInfoReturnable<Float> cir) {
        if (this.level().isClientSide()) return;
        Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
        if (this.hasEffect(invisibility) && Objects.requireNonNull(this.getEffect(invisibility)).getAmplifier() > 0) cir.setReturnValue(0.0F);
    }

    // endregion
}