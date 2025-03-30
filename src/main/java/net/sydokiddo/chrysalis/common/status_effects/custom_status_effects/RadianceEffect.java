package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.sydokiddo.chrysalis.common.misc.CParticles;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes.GenericStatusEffect;
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