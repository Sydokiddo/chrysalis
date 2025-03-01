package net.sydokiddo.chrysalis.registry.status_effects;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects.GenericStatusEffect;
import net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects.MobSightEffect;
import java.awt.*;

@SuppressWarnings("unused")
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

    public static final Holder<MobEffect> HEALTH_REDUCTION = registerStatusEffect("health_reduction",
        new GenericStatusEffect(MobEffectCategory.HARMFUL, Color.decode("#994C4C").getRGB())
        .addAttributeModifier(Attributes.MAX_HEALTH, ChrysalisMod.id("effect.health_reduction"), -4.0D, AttributeModifier.Operation.ADD_VALUE));

    public static final Holder<MobEffect> BUILDING_FATIGUE = registerStatusEffect("building_fatigue",
        new GenericStatusEffect(MobEffectCategory.HARMFUL, Color.decode("#79553A").getRGB()));

    // endregion

    // region Registry

    public static Holder<MobEffect> registerStatusEffect(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ChrysalisMod.id(name), statusEffect);
    }

    public static void registerStatusEffects() {}

    // endregion
}