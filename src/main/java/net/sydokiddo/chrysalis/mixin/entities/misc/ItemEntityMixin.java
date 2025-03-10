package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDataComponents;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameRules;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Unique ItemEntity chrysalis$itemEntity = (ItemEntity) (Object) this;
    @Shadow public abstract ItemStack getItem();
    @Shadow public abstract void setExtendedLifetime();
    @Shadow private int pickupDelay;

    private ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Adds the glow color entity data to item entities.
     **/

    @Unique private final String chrysalis$glowColorTag = "glow_color";

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void chrysalis$defineItemEntityTags(SynchedEntityData.Builder builder, CallbackInfo info) {
        // builder.define(ChrysalisRegistry.ITEM_GLOW_COLOR, 16777215);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void chrysalis$addItemEntityTags(CompoundTag compoundTag, CallbackInfo info) {
        compoundTag.putInt(this.chrysalis$glowColorTag, ItemHelper.getItemGlowColor(this.chrysalis$itemEntity));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void chrysalis$readItemEntityTags(CompoundTag compoundTag, CallbackInfo info) {
        if (compoundTag.get(this.chrysalis$glowColorTag) != null) ItemHelper.setItemGlowColor(this.chrysalis$itemEntity, compoundTag.getInt(this.chrysalis$glowColorTag));
    }

    @Override
    public int getTeamColor() {
        if (this.getTeam() == null) return ItemHelper.getItemGlowColor(this.chrysalis$itemEntity);
        return super.getTeamColor();
    }

    /**
     * Any items in the increased_despawn_time tag will set themselves to have an extended lifetime on their first tick.
     **/

    @Inject(at = @At("HEAD"), method = "tick")
    private void chrysalis$makeItemsHaveExtendedLifetime(CallbackInfo info) {
        if (!this.getItem().isEmpty() && this.firstTick && (this.getItem().is(ChrysalisTags.INCREASED_DESPAWN_TIME)) || this.getItem().has(ChrysalisDataComponents.INCREASED_DESPAWN_TIME)) this.setExtendedLifetime();
    }

    /**
     * Any items in the immune_to_despawning tag will never be able to despawn.
     **/

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V", ordinal = 1))
    private void chrysalis$makeItemsNeverDespawn(ItemEntity itemEntity) {
        if ((this.getItem().is(ChrysalisTags.IMMUNE_TO_DESPAWNING) || this.getItem().has(ChrysalisDataComponents.IMMUNE_TO_DESPAWNING)) && this.pickupDelay != Short.MAX_VALUE) return;
        this.discard();
    }

    /**
     * Makes items immune to specific damage sources.
     **/

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    private void chrysalis$makeItemsImmune(ServerLevel serverLevel, DamageSource damageSource, float damageAmount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.getItem().isEmpty()) {
            if ((this.getItem().is(ChrysalisTags.IMMUNE_TO_DAMAGE) || this.getItem().has(ChrysalisDataComponents.IMMUNE_TO_ALL_DAMAGE)) && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) cir.setReturnValue(false);
            if (!serverLevel.getGameRules().getBoolean(ChrysalisGameRules.RULE_DESTROY_ITEMS_IN_EXPLOSIONS) && damageSource.is(DamageTypeTags.IS_EXPLOSION)) cir.setReturnValue(false);
        }
    }
}