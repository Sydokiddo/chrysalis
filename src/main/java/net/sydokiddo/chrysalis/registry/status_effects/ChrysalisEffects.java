package net.sydokiddo.chrysalis.registry.status_effects;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects.GenericStatusEffect;
import java.awt.*;

public class ChrysalisEffects {

    public static final Holder<MobEffect> RADIANCE = registerStatusEffect("radiance",
    new GenericStatusEffect(MobEffectCategory.HARMFUL, Color.decode("#FFFFB2").getRGB(), ChrysalisParticles.RADIANCE).setBlendDuration(5));

    public static Holder<MobEffect> registerStatusEffect(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Chrysalis.id(name), statusEffect);
    }

    public static void registerStatusEffects() {}
}