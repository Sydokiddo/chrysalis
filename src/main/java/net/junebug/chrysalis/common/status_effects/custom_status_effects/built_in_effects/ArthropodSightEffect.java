package net.junebug.chrysalis.common.status_effects.custom_status_effects.built_in_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.junebug.chrysalis.common.misc.CParticles;
import net.junebug.chrysalis.common.status_effects.custom_status_effects.MobSightEffect;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class ArthropodSightEffect extends MobSightEffect {

    public ArthropodSightEffect() {
        super(Color.decode("#A80E0E").getRGB());
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance effect) {
        return CParticles.ARTHROPOD_SIGHT.get();
    }
}