package net.junebug.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlyStraightTowardsParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.junebug.chrysalis.client.particles.options.ColoredDirectionalDustParticleOptions;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ColoredDirectionalDustParticle extends FlyStraightTowardsParticle {

    /**
     * A version of the ominous item spawner's directional dust particles that can have its color configured.
     **/

    public ColoredDirectionalDustParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ColoredDirectionalDustParticleOptions particleOptions) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, particleOptions.startingColor(), particleOptions.endingColor());
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ColoredDirectionalDustParticleOptions> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull ColoredDirectionalDustParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ColoredDirectionalDustParticle particle = new ColoredDirectionalDustParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, particleOptions);
            particle.scale(Mth.randomBetween(particle.random, 3.0F, 5.0F));
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}