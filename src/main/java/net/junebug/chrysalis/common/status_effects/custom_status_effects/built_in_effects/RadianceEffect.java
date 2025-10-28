package net.junebug.chrysalis.common.status_effects.custom_status_effects.built_in_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.junebug.chrysalis.common.misc.CParticles;
import net.junebug.chrysalis.common.status_effects.custom_status_effects.GenericStatusEffect;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class RadianceEffect extends GenericStatusEffect {

    public RadianceEffect() {
        super(MobEffectCategory.HARMFUL, Color.decode("#FFFFB2").getRGB());
        this.setBlendDuration(5);
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance effect) {
        return CParticles.RADIANCE.get();
    }
}