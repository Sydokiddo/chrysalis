package net.sydokiddo.chrysalis.common.status_effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.ArthropodSightEffect;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.CreeperSightEffect;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.EnderSightEffect;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.RadianceEffect;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes.GenericStatusEffect;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes.BuildPreventingEffect;
import java.awt.*;

public class ChrysalisEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Chrysalis.MOD_ID);

    // region Status Effects

    @SuppressWarnings("unused")
    public static final DeferredHolder<MobEffect, MobEffect>
        RADIANCE = MOB_EFFECTS.register("radiance", RadianceEffect::new),
        ARTHROPOD_SIGHT = MOB_EFFECTS.register("arthropod_sight", ArthropodSightEffect::new),
        CREEPER_SIGHT = MOB_EFFECTS.register("creeper_sight", CreeperSightEffect::new),
        ENDER_SIGHT = MOB_EFFECTS.register("ender_sight", EnderSightEffect::new),
        HEALTH_REDUCTION = MOB_EFFECTS.register("health_reduction", () -> new GenericStatusEffect(MobEffectCategory.HARMFUL, Color.decode("#994C4C").getRGB()).addAttributeModifier(Attributes.MAX_HEALTH, Chrysalis.resourceLocationId("effect.health_reduction"), -4.0D, AttributeModifier.Operation.ADD_VALUE)),
        BUILDING_FATIGUE = MOB_EFFECTS.register("building_fatigue", () -> new BuildPreventingEffect(MobEffectCategory.HARMFUL, Color.decode("#79553A").getRGB()))
    ;

    // endregion

    // region Registry

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

    // endregion
}