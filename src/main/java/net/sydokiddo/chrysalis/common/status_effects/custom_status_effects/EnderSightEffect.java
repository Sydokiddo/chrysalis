package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.sydokiddo.chrysalis.common.misc.ChrysalisParticles;
import net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes.MobSightEffect;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class EnderSightEffect extends MobSightEffect {

    public EnderSightEffect() {
        super(Color.decode("#E079FA").getRGB());
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance effect) {
        return ChrysalisParticles.ENDER_SIGHT.get();
    }
}