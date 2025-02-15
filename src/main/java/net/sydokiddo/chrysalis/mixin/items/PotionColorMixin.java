package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.awt.*;

@Mixin(MobEffect.class)
public class PotionColorMixin {

    @Shadow @Final private int color;

    /**
     * Changes the default potion colors of blindness, haste, mining fatigue, and wither.
     **/

    @Inject(at = @At("RETURN"), method = "getColor", cancellable = true)
    private void chrysalis$changeEffectColors(CallbackInfoReturnable<Integer> cir) {
        if (this.color == 2039587) cir.setReturnValue(Color.decode("#593F7F").getRGB()); // Blindness
        if (this.color == 14270531) cir.setReturnValue(Color.decode("#FFB87F").getRGB()); // Haste
        if (this.color == 4866583) cir.setReturnValue(Color.decode("#5959B2").getRGB()); // Mining Fatigue
        if (this.color == 7561558) cir.setReturnValue(Color.decode("#764857").getRGB()); // Wither
    }
}