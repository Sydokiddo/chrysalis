package net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class GenericStatusEffect extends MobEffect {

    public GenericStatusEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public GenericStatusEffect(MobEffectCategory mobEffectCategory, int color, ParticleOptions particleType) {
        super(mobEffectCategory, color, particleType);
    }
}