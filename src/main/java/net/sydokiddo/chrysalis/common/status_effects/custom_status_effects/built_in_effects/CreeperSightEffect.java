package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.built_in_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.sydokiddo.chrysalis.common.misc.CParticles;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.MobSightEffect;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class CreeperSightEffect extends MobSightEffect {

    public CreeperSightEffect() {
        super(Color.decode("#459333").getRGB());
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance effect) {
        return CParticles.CREEPER_SIGHT.get();
    }
}