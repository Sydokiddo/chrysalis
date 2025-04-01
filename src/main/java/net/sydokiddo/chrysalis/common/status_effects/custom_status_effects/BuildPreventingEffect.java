package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects;

import net.minecraft.world.effect.MobEffectCategory;

public class BuildPreventingEffect extends GenericStatusEffect {

    /**
     * A status effect class that prevents players who are affected by it from being able to place blocks.
     **/

    public BuildPreventingEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
}