package net.sydokiddo.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.client.particles.options.SparkParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class SparkParticle extends RotatingFallingParticle implements ParticleCommonMethods {

    /**
     * A falling spark particle that fades out, with options to configure the color and scale.
     **/

    // region Initialization and Ticking

    public SparkParticle(ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SparkParticleOptions particleOptions, SpriteSet spriteSet) {
        super(level, x, y, z, velocityX, velocityY, velocityZ);

        this.pickSprite(spriteSet);
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

    // region Rendering

    @Override
    public float getQuadSize(float tickRate) {
        return this.shrinkParticle(this.quadSize, ((float) this.age + tickRate) / (float) this.lifetime);
    }

    @Override
    public int getLightColor(float tickRate) {
        return this.fadeLightColor(1.0F, 0.0F, this.age, this.lifetime, super.getLightColor(tickRate));
    }

    // endregion

    // region Providers

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SparkParticleOptions> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SparkParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SparkParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ, particleOptions, this.spriteSet);
        }
    }

    // endregion
}