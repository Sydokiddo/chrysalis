package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffect.class)
public abstract class PotionColorMixin {

    @Shadow @Final private int color;

    /**
     * Changes the default potion colors of Blindness, Haste, and Mining Fatigue.
     **/

    @Inject(at = @At("RETURN"), method = "getColor", cancellable = true)
    private void chrysalis_changeEffectColors(CallbackInfoReturnable<Integer> cir) {

        // Blindness

        if (this.color == 2039587) {
            cir.setReturnValue(5848959);
        }

        // Haste

        if (this.color == 14270531) {
            cir.setReturnValue(16758911);
        }

        // Mining Fatigue

        if (this.color == 4866583) {
            cir.setReturnValue(5855666);
        }
    }
}