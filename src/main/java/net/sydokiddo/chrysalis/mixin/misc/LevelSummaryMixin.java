package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.world.level.storage.LevelSummary;
import net.sydokiddo.chrysalis.Chrysalis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelSummary.class)
public class LevelSummaryMixin {

    @Inject(method = "isExperimental", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$disableExperimentalWarning(CallbackInfoReturnable<Boolean> cir) {
        if (Chrysalis.IS_DEBUG) cir.setReturnValue(false);
    }
}