package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.sydokiddo.chrysalis.client.particles.options.SparkleParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class SparkleParticle extends FadingEmissiveParticle {

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

    @Environment(EnvType.CLIENT)
    public static class SparkleParticleProvider implements ParticleProvider<SparkleParticleOptions> {

        private final SpriteSet spriteSet;

        public SparkleParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SparkleParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SparkleParticle(clientLevel, x, y, z, particleOptions, this.spriteSet);
        }
    }

    // endregion
}