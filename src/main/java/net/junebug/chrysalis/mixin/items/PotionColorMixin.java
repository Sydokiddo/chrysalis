package net.junebug.chrysalis.mixin.items;

import net.junebug.chrysalis.common.CConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.junebug.chrysalis.util.helpers.ComponentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;

@Mixin(MobEffect.class)
public abstract class PotionColorMixin {

    @Shadow public abstract String getDescriptionId();
    @Shadow @Final private static int AMBIENT_ALPHA;

    /**
     * Changes the default potion colors of blindness, haste, mining fatigue, and wither.
     **/

    @Inject(at = @At("RETURN"), method = "getColor", cancellable = true)
    private void chrysalis$changeEffectColors(CallbackInfoReturnable<Integer> cir) {
        if (!CConfig.MODIFIED_EFFECT_COLORS.get()) return;
        if (this.chrysalis$isEffect(MobEffects.BLINDNESS)) cir.setReturnValue(ComponentHelper.BLINDNESS_COLOR.getRGB());
        if (this.chrysalis$isEffect(MobEffects.DIG_SPEED)) cir.setReturnValue(ComponentHelper.HASTE_COLOR.getRGB());
        if (this.chrysalis$isEffect(MobEffects.DIG_SLOWDOWN)) cir.setReturnValue(ComponentHelper.MINING_FATIGUE_COLOR.getRGB());
        if (this.chrysalis$isEffect(MobEffects.WITHER)) cir.setReturnValue(ComponentHelper.WITHER_COLOR.getRGB());
    }

    @Inject(at = @At("RETURN"), method = "createParticleOptions", cancellable = true)
    private void chrysalis$changeEffectParticles(MobEffectInstance effect, CallbackInfoReturnable<ParticleOptions> cir) {
        if (!CConfig.MODIFIED_EFFECT_COLORS.get()) return;
        if (this.chrysalis$isEffect(MobEffects.BLINDNESS)) cir.setReturnValue(this.chrysalis$createDefaultParticle(effect, ComponentHelper.BLINDNESS_COLOR.getRGB()));
        if (this.chrysalis$isEffect(MobEffects.DIG_SPEED)) cir.setReturnValue(this.chrysalis$createDefaultParticle(effect, ComponentHelper.HASTE_COLOR.getRGB()));
        if (this.chrysalis$isEffect(MobEffects.DIG_SLOWDOWN)) cir.setReturnValue(this.chrysalis$createDefaultParticle(effect, ComponentHelper.MINING_FATIGUE_COLOR.getRGB()));
        if (this.chrysalis$isEffect(MobEffects.WITHER)) cir.setReturnValue(this.chrysalis$createDefaultParticle(effect, ComponentHelper.WITHER_COLOR.getRGB()));
    }

    @Unique
    private boolean chrysalis$isEffect(Holder<MobEffect> effect) {
        return Objects.equals(this.getDescriptionId(), effect.value().getDescriptionId());
    }

    @Unique
    private ColorParticleOption chrysalis$createDefaultParticle(MobEffectInstance effect, int color) {
        return ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(effect.isAmbient() ? AMBIENT_ALPHA : 255, color));
    }
}