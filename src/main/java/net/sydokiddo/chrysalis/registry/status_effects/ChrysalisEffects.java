package net.sydokiddo.chrysalis.registry.status_effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects.GenericStatusEffect;
import net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects.MobSightEffect;
import java.awt.*;
import java.util.function.Supplier;

public class ChrysalisEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, ChrysalisMod.MOD_ID);

    // region Status Effects

    @SuppressWarnings("unused")
    public static final Supplier<MobEffect>
        RADIANCE = MOB_EFFECTS.register("radiance", () -> new GenericStatusEffect(MobEffectCategory.HARMFUL, Color.decode("#FFFFB2").getRGB()).setBlendDuration(5)),
        ARTHROPOD_SIGHT = MOB_EFFECTS.register("arthropod_sight", () -> new MobSightEffect(MobEffectCategory.NEUTRAL, Color.decode("#A80E0E").getRGB())),
        CREEPER_SIGHT = MOB_EFFECTS.register("creeper_sight", () -> new MobSightEffect(MobEffectCategory.NEUTRAL, Color.decode("#459333").getRGB())),
        ENDER_SIGHT = MOB_EFFECTS.register("ender_sight", () -> new MobSightEffect(MobEffectCategory.NEUTRAL, Color.decode("#E079FA").getRGB())),
        HEALTH_REDUCTION = MOB_EFFECTS.register("health_reduction", () -> new GenericStatusEffect(MobEffectCategory.HARMFUL, Color.decode("#994C4C").getRGB()).addAttributeModifier(Attributes.MAX_HEALTH, ChrysalisMod.id("effect.health_reduction"), -4.0D, AttributeModifier.Operation.ADD_VALUE)),
        BUILDING_FATIGUE = MOB_EFFECTS.register("building_fatigue", () -> new GenericStatusEffect(MobEffectCategory.HARMFUL, Color.decode("#79553A").getRGB()))
    ;

    // endregion

    // region Registry

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

    // endregion
}