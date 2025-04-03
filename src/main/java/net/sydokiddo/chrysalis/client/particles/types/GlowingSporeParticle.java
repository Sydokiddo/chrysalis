package net.sydokiddo.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class GlowingSporeParticle extends FadingEmissiveParticle implements ParticleCommonMethods {

    /**
     * A floating spore particle effect that shrinks over time.
     **/

    // region Initialization and Ticking

    public GlowingSporeParticle(ClientLevel clientLevel, SpriteSet spriteSet, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(clientLevel, x, y, z, 1.0F, 0.0F, spriteSet, false);
        this.setSize(0.001F, 0.001F);
        this.lifetime = (int) (8.0D / (this.random.nextDouble() * 0.8D + 0.2D)) + 4;
        this.hasPhysics = true;
        this.xd = velocityX;
        this.yd = velocityY;
        this.zd = velocityZ;
        this.pickSprite(spriteSet);
    }

    // endregion

    // region Rendering

    @Override
    public float getQuadSize(float tickRate) {
        return this.shrinkParticle(this.quadSize, ((float) this.age + tickRate) / (float) this.lifetime);
    }

    // endregion

    // region Providers

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new GlowingSporeParticle(clientLevel, this.spriteSet, x, y, z, velocityX, velocityY, velocityZ);
        }
    }

    // endregion
}