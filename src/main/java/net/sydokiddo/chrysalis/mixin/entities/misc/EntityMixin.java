package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.sydokiddo.chrysalis.registry.items.ChrysalisDebugItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique Entity entity = (Entity) (Object) this;
    @Shadow public abstract boolean isAttackable();

    @Inject(method = "getPickRadius", at = @At("RETURN"), cancellable = true)
    private void chrysalis$killWandPickRadius(CallbackInfoReturnable<Float> cir) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.getMainHandItem().is(ChrysalisDebugItems.KILL_WAND) && this.isAttackable()) cir.setReturnValue(minecraft.player.distanceTo(this.entity) > 8 ? 0.5F : 0.0F);
    }
}