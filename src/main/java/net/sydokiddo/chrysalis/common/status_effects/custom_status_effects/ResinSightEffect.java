package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.sydokiddo.chrysalis.common.misc.CParticles;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes.MobSightEffect;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class ResinSightEffect extends MobSightEffect {

    public ResinSightEffect() {
        super(Color.decode("#F3791B").getRGB());
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance effect) {
        return CParticles.RESIN_SIGHT.get();
    }
}