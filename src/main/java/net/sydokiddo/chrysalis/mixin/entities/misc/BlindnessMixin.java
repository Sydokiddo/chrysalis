package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class BlindnessMixin extends Entity {

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot var1);

    private BlindnessMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private boolean hasMobHead(Entity entity) {

        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.HEAD);
        EntityType<?> entityType = entity.getType();

        return (entityType == EntityType.SKELETON && itemStack.is(Items.SKELETON_SKULL) || entityType == EntityType.ZOMBIE && itemStack.is(Items.ZOMBIE_HEAD) ||
        entityType == EntityType.PIGLIN && itemStack.is(Items.PIGLIN_HEAD) || entityType == EntityType.PIGLIN_BRUTE && itemStack.is(Items.PIGLIN_HEAD) ||
        entityType == EntityType.CREEPER && itemStack.is(Items.CREEPER_HEAD) || entityType.is(ChrysalisTags.ENDER_RELATED_MOBS) && itemStack.is(Items.CARVED_PUMPKIN));
    }

    @Inject(method = "getVisibilityPercent", at = @At(value = "HEAD"), cancellable = true)
    private void endlessEncore_makeMobsAffectedByBlindness(Entity entity, CallbackInfoReturnable<Double> cir) {

        // Mobs are now affected by Blindness, which decreases their visibility depending on the amplifier of the effect

        if (entity instanceof LivingEntity livingEntity && !this.level().isClientSide() && livingEntity.hasEffect(MobEffects.BLINDNESS)) {

            int amplifier = Objects.requireNonNull(livingEntity.getEffect(MobEffects.BLINDNESS)).getAmplifier();

            double beforeViewDistance = 1.0;
            double beforeViewDistanceWithHeadOn = 0.5;

            double afterViewDistance = beforeViewDistance / (2 * (amplifier + 1));
            double afterViewDistanceWithHeadOn = beforeViewDistanceWithHeadOn / (2 * (amplifier + 1));

            if (hasMobHead(entity)) {
                cir.setReturnValue(afterViewDistanceWithHeadOn);
            } else {
                cir.setReturnValue(afterViewDistance);
            }
        }

        // Ender mobs have reduced visibility to players wearing Carved Pumpkins

        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.HEAD);
        EntityType<?> entityType = entity.getType();
        if (entityType.is(ChrysalisTags.ENDER_RELATED_MOBS) && itemStack.is(Items.CARVED_PUMPKIN)) {
            cir.setReturnValue(0.5);
        }
    }
}