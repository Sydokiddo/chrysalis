package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Shadow public abstract ItemStack getItem();

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    // Items in the immune_to_damage tag will not take damage from most damage sources

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void chrysalis_makeItemsImmune(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (!this.getItem().isEmpty()) {
            if (this.getItem().is(ChrysalisTags.IMMUNE_TO_DAMAGE)) {
                if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
