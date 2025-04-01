package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class GenericStatusEffect extends MobEffect {

    /**
     * A generic status effect class that can be extended by any other status effect class.
     **/

    public GenericStatusEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
}