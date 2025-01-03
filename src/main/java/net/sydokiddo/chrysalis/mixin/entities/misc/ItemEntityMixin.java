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
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
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

    @Unique ItemEntity itemEntity = (ItemEntity) (Object) this;
    @Shadow public abstract ItemStack getItem();
    @Shadow public abstract void setExtendedLifetime();
    @Shadow private int pickupDelay;

    private ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Adds the glow color entity data to item entities.
     **/

    @Unique private final String glowColorTag = "GlowColor";

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void chrysalis$defineItemEntityTags(SynchedEntityData.Builder builder, CallbackInfo info) {
        builder.define(ChrysalisRegistry.ITEM_GLOW_COLOR, 16777215);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void chrysalis$addItemEntityTags(CompoundTag compoundTag, CallbackInfo info) {
        compoundTag.putInt(this.glowColorTag, ItemHelper.getItemGlowColor(this.itemEntity));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void chrysalis$readItemEntityTags(CompoundTag compoundTag, CallbackInfo info) {
        if (compoundTag.get(this.glowColorTag) != null) ItemHelper.setItemGlowColor(this.itemEntity, compoundTag.getInt(this.glowColorTag));
    }

    @Override
    public int getTeamColor() {
        if (this.getTeam() == null) return ItemHelper.getItemGlowColor(this.itemEntity);
        return super.getTeamColor();
    }

    /**
     * Any items in the increased_despawn_time tag will set themselves to have an extended lifetime on the first tick.
     **/

    @Inject(at = @At("HEAD"), method = "tick")
    private void chrysalis$makeItemsHaveExtendedLifetime(CallbackInfo info) {
        if (!this.getItem().isEmpty() && this.firstTick && this.getItem().is(ChrysalisTags.INCREASED_DESPAWN_TIME)) this.setExtendedLifetime();
    }

    /**
     * Any items in the immune_to_despawning tag will never be able to despawn.
     **/

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V", ordinal = 1))
    private void chrysalis$makeItemsNeverDespawn(ItemEntity itemEntity) {
        if (this.getItem().is(ChrysalisTags.IMMUNE_TO_DESPAWNING) && this.pickupDelay != Short.MAX_VALUE) return;
        this.discard();
    }

    /**
     * Makes various items immune to various damage sources dependent on what tag they are in.
     **/

    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    private void chrysalis$makeItemsImmune(ServerLevel serverLevel, DamageSource damageSource, float damageAmount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.getItem().isEmpty()) {
            if (this.getItem().is(ChrysalisTags.IMMUNE_TO_DAMAGE) && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) cir.setReturnValue(false);
            if ((this.getItem().is(ChrysalisTags.IMMUNE_TO_EXPLOSIONS) || !serverLevel.getGameRules().getBoolean(ChrysalisRegistry.RULE_DESTROY_ITEMS_IN_EXPLOSIONS)) && damageSource.is(DamageTypeTags.IS_EXPLOSION)) cir.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "fireImmune", cancellable = true)
    private void chrysalis$makeItemsFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (this.getItem().is(ChrysalisTags.IMMUNE_TO_FIRE)) cir.setReturnValue(true);
    }
}