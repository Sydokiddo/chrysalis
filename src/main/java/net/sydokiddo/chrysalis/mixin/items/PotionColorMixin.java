package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.awt.*;

@Mixin(MobEffect.class)
public abstract class PotionColorMixin {

    @Shadow public abstract String getDescriptionId();

    /**
     * Changes the default potion colors of blindness, haste, mining fatigue, and wither.
     **/

    @Inject(at = @At("RETURN"), method = "getColor", cancellable = true)
    private void chrysalis$changeEffectColors(CallbackInfoReturnable<Integer> cir) {
        if (this.chrysalis$effectEquals(MobEffects.BLINDNESS)) cir.setReturnValue(Color.decode("#593F7F").getRGB());
        if (this.chrysalis$effectEquals(MobEffects.DIG_SPEED)) cir.setReturnValue(Color.decode("#FFB87F").getRGB());
        if (this.chrysalis$effectEquals(MobEffects.DIG_SLOWDOWN)) cir.setReturnValue(Color.decode("#5959B2").getRGB()); // Mining Fatigue
        if (this.chrysalis$effectEquals(MobEffects.WITHER)) cir.setReturnValue(Color.decode("#764857").getRGB()); // Wither
    }

    @Unique
    private boolean chrysalis$effectEquals(Holder<MobEffect> mobEffect) {
        return this.getDescriptionId().equals(mobEffect.getRegisteredName());
    }
}