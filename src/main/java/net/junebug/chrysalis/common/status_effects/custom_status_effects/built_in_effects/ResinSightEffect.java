package net.junebug.chrysalis.common.status_effects.custom_status_effects.built_in_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.junebug.chrysalis.common.misc.CParticles;
import net.junebug.chrysalis.common.status_effects.custom_status_effects.MobSightEffect;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class ResinSightEffect extends MobSightEffect {

    /**
     * A status effect that renders the player's screen with the resin shader.
     **/

    public ResinSightEffect() {
        super(Color.decode("#F3791B").getRGB());
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance mobEffectInstance) {
        return CParticles.RESIN_SIGHT.get();
    }
}