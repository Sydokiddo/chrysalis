package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique Entity entity = (Entity) (Object) this;
    @Shadow public abstract boolean isAttackable();

    @Inject(method = "getPickRadius", at = @At("RETURN"), cancellable = true)
    private void chrysalis$increasedPickRadius(CallbackInfoReturnable<Float> cir) {

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        ItemStack itemStack = minecraft.player.getMainHandItem();

        if (itemStack.is(ChrysalisDebugItems.KILL_WAND) && !this.isAttackable()) return;
        if (itemStack.is(ChrysalisTags.INCREASED_PICK_RADIUS)) cir.setReturnValue(minecraft.player.distanceTo(this.entity) > 8 ? 0.5F : 0.0F);
    }

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventEntityRendering(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        Holder<MobEffect> invisibility = MobEffects.INVISIBILITY;
        if (this.entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(invisibility) && Objects.requireNonNull(livingEntity.getEffect(invisibility)).getAmplifier() > 0) cir.setReturnValue(false);
    }
}