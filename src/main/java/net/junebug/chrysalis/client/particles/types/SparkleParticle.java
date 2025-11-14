package net.junebug.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.junebug.chrysalis.client.particles.options.SparkleParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class SparkleParticle extends FadingEmissiveParticle {

    /**
     * A sparkle particle with the ability to configure the color.
     **/

    // region Initialization

    public SparkleParticle(ClientLevel clientLevel, double x, double y, double z, SparkleParticleOptions particleOptions, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 1.0F, 0.0F, spriteSet, true);

        this.lifetime = 10;
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);

        Vector3f color = particleOptions.getFinalColor();

        if (particleOptions.shouldRandomizeColor()) {
            this.rCol = particleOptions.randomizeColor(color.x(), this.random);
            this.gCol = particleOptions.randomizeColor(color.y(), this.random);
            this.bCol = particleOptions.randomizeColor(color.z(), this.random);
        } else {
            this.rCol = color.x();
            this.gCol = color.y();
            this.bCol = color.z();
        }
    }

    // endregion

    // region Providers

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SparkleParticleOptions> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SparkleParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SparkleParticle(clientLevel, x, y, z, particleOptions, this.spriteSet);
        }
    }

    // endregion
}