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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Shadow public abstract ItemStack getItem();
    @Shadow public abstract void setExtendedLifetime();

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    // Items in the increased_despawn_time tag will take longer to despawn

    @Inject(at = @At("HEAD"), method = "tick")
    private void chrysalis_makeItemsHaveExtendedLifetime(CallbackInfo ci) {
        if (!this.getItem().isEmpty()) {
            if (this.getItem().is(ChrysalisTags.INCREASED_DESPAWN_TIME) && this.firstTick) {
                this.setExtendedLifetime();
            }
        }
    }

    // Items in the immune_to_despawning tag will never despawn

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V"))
    public void chrysalis_makeItemsNeverDespawn(ItemEntity instance) {
        if (!this.getItem().is(ChrysalisTags.IMMUNE_TO_DESPAWNING)) {
            this.discard();
        }
    }

    // Makes various items immune to various damage sources

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void chrysalis_makeItemsImmune(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (!this.getItem().isEmpty()) {
            if (this.getItem().is(ChrysalisTags.IMMUNE_TO_DAMAGE)) {
                if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                    cir.setReturnValue(false);
                }
            }
            if (this.getItem().is(ChrysalisTags.IMMUNE_TO_EXPLOSIONS)) {
                if (damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
