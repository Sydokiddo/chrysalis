package net.sydokiddo.chrysalis.mixin.entities.hostile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public class WardenMixin {

    /**
     * Wardens will ignore any entities in the warden_ignored tag.
     **/

    @Inject(at = @At("HEAD"), method = "canTargetEntity", cancellable = true)
    private void chrysalis$wardenIgnoredTag(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.getType().is(ChrysalisTags.WARDEN_IGNORED)) cir.setReturnValue(false);
    }
}