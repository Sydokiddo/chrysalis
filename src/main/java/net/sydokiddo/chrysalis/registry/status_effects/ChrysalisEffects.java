package net.sydokiddo.chrysalis.registry.status_effects;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects.GenericStatusEffect;
import net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects.MobSightEffect;
import java.awt.*;

public class ChrysalisEffects {

    // region Status Effects

    public static final Holder<MobEffect> RADIANCE = registerStatusEffect("radiance",
    new GenericStatusEffect(MobEffectCategory.HARMFUL, Color.decode("#FFFFB2").getRGB(), ChrysalisParticles.RADIANCE).setBlendDuration(5));

    public static final Holder<MobEffect> ARTHROPOD_SIGHT = registerStatusEffect("arthropod_sight",
    new MobSightEffect(MobEffectCategory.NEUTRAL, Color.decode("#A80E0E").getRGB(), ChrysalisParticles.ARTHROPOD_SIGHT));

    public static final Holder<MobEffect> CREEPER_SIGHT = registerStatusEffect("creeper_sight",
    new MobSightEffect(MobEffectCategory.NEUTRAL, Color.decode("#459333").getRGB(), ChrysalisParticles.CREEPER_SIGHT));

    public static final Holder<MobEffect> ENDER_SIGHT = registerStatusEffect("ender_sight",
    new MobSightEffect(MobEffectCategory.NEUTRAL, Color.decode("#E079FA").getRGB(), ChrysalisParticles.ENDER_SIGHT));

    // endregion

    // region Registry

    public static Holder<MobEffect> registerStatusEffect(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Chrysalis.id(name), statusEffect);
    }

    public static void registerStatusEffects() {}

    // endregion
}