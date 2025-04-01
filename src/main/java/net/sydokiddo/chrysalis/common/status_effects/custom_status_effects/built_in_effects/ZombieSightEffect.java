package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.built_in_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.sydokiddo.chrysalis.common.misc.CParticles;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.MobSightEffect;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class ZombieSightEffect extends MobSightEffect {

    public ZombieSightEffect() {
        super(Color.decode("#B44420").getRGB());
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance effect) {
        return CParticles.ZOMBIE_SIGHT.get();
    }
}