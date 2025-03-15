package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.sydokiddo.chrysalis.common.misc.ChrysalisParticles;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes.MobSightEffect;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class CreeperSightEffect extends MobSightEffect {

    public CreeperSightEffect() {
        super(Color.decode("#459333").getRGB());
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance effect) {
        return ChrysalisParticles.CREEPER_SIGHT.get();
    }
}