package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);

    private LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    // region Reworked Blindness

    /**
     * Checks to see if the user is wearing a mob head for the designated mob that it reduces the visibility percentage of.
     **/

    @Unique
    private boolean hasMobHead(Entity entity) {

        EntityType<?> entityType = entity.getType();
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.HEAD);

        return (
            entityType == EntityType.SKELETON && itemStack.is(Items.SKELETON_SKULL) ||
            entityType == EntityType.ZOMBIE && itemStack.is(Items.ZOMBIE_HEAD) ||
            entityType == EntityType.PIGLIN && itemStack.is(Items.PIGLIN_HEAD) ||
            entityType == EntityType.PIGLIN_BRUTE && itemStack.is(Items.PIGLIN_HEAD) ||
            entityType == EntityType.CREEPER && itemStack.is(Items.CREEPER_HEAD) ||
            this.isEnder(entity)
        );
    }

    @Unique
    private boolean isEnder(Entity entity) {
        return entity.getType().is(ChrysalisTags.ENDER) && this.getItemBySlot(EquipmentSlot.HEAD).is(ChrysalisTags.PROTECTS_AGAINST_ENDERMEN);
    }

    @Unique
    private static double viewDistanceDivision(double baseViewDistance, int amplifier) {
        return baseViewDistance / (2.0D * (amplifier + 1));
    }

    /**
     * Mobs are now affected by the Blindness status effect, which decreases their visibility percentage depending on the amplifier of the effect.
     * <p>
     * Additionally, Ender mobs now have reduced visibility of entities wearing anything in their helmet slot that is in the protects_against_endermen tag.
     **/

    @Inject(method = "getVisibilityPercent", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$makeMobsAffectedByBlindness(Entity entity, CallbackInfoReturnable<Double> cir) {

        if (this.level().isClientSide() || !(entity instanceof LivingEntity livingEntity)) return;

        if (livingEntity.hasEffect(MobEffects.BLINDNESS)) {
            int blindnessAmplifier = Objects.requireNonNull(livingEntity.getEffect(MobEffects.BLINDNESS)).getAmplifier();
            if (this.hasMobHead(livingEntity)) cir.setReturnValue(viewDistanceDivision(0.5D, blindnessAmplifier));
            else cir.setReturnValue(viewDistanceDivision(1.0D, blindnessAmplifier));
        } else {
            if (this.isEnder(livingEntity)) cir.setReturnValue(0.5D);
        }
    }

    // endregion
}