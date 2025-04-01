package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.built_in_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.sydokiddo.chrysalis.common.misc.CParticles;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.MobSightEffect;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class BlindSightEffect extends MobSightEffect {

    public BlindSightEffect() {
        super(Color.decode("#05625D").getRGB());
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance effect) {
        return CParticles.BLIND_SIGHT.get();
    }
}