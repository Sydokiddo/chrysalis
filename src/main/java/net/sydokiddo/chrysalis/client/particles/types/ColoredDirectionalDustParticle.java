package net.sydokiddo.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlyStraightTowardsParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.options.ColoredDirectionalDustParticleOptions;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ColoredDirectionalDustParticle extends FlyStraightTowardsParticle {

    public ColoredDirectionalDustParticle(ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, ColoredDirectionalDustParticleOptions particleOptions) {
        super(level, x, y, z, velocityX, velocityY, velocityZ, particleOptions.startingColor(), particleOptions.endingColor());
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ColoredDirectionalDustParticleOptions> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull ColoredDirectionalDustParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            ColoredDirectionalDustParticle particle = new ColoredDirectionalDustParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ, particleOptions);
            particle.scale(Mth.randomBetween(clientLevel.getRandom(), 3.0F, 5.0F));
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}