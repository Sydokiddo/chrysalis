package net.junebug.chrysalis.common.status_effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.status_effects.custom_status_effects.GenericStatusEffect;
import net.junebug.chrysalis.common.status_effects.custom_status_effects.BuildPreventingEffect;
import net.junebug.chrysalis.common.status_effects.custom_status_effects.built_in_effects.*;
import java.awt.*;

public class CStatusEffects {

    /**
     * The registry for status effects added by chrysalis.
     **/

    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Chrysalis.MOD_ID);

    // region Status Effects

    @SuppressWarnings("unused")
    public static final DeferredHolder<MobEffect, MobEffect>
        RADIANCE = MOB_EFFECTS.register("radiance", RadianceEffect::new),
        HEALTH_REDUCTION = MOB_EFFECTS.register("health_reduction", () -> new GenericStatusEffect(MobEffectCategory.HARMFUL, Color.decode("#B53F6B").getRGB()).addAttributeModifier(Attributes.MAX_HEALTH, Chrysalis.resourceLocationId("effect.health_reduction"), -4.0D, AttributeModifier.Operation.ADD_VALUE)),
        BUILDING_FATIGUE = MOB_EFFECTS.register("building_fatigue", () -> new BuildPreventingEffect(MobEffectCategory.HARMFUL, Color.decode("#79553A").getRGB())),
        ARTHROPOD_SIGHT = MOB_EFFECTS.register("arthropod_sight", ArthropodSightEffect::new),
        BLIND_SIGHT = MOB_EFFECTS.register("blind_sight", BlindSightEffect::new),
        CREEPER_SIGHT = MOB_EFFECTS.register("creeper_sight", CreeperSightEffect::new),
        ENDER_SIGHT = MOB_EFFECTS.register("ender_sight", EnderSightEffect::new),
        RESIN_SIGHT = MOB_EFFECTS.register("resin_sight", ResinSightEffect::new),
        SKELETAL_SIGHT = MOB_EFFECTS.register("skeletal_sight", SkeletalSightEffect::new),
        ZOMBIE_SIGHT = MOB_EFFECTS.register("zombie_sight", ZombieSightEffect::new)
    ;

    // endregion

    // region Registry

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

    // endregion
}