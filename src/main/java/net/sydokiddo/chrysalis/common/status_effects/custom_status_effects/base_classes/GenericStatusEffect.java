package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class GenericStatusEffect extends MobEffect {

    public GenericStatusEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
}