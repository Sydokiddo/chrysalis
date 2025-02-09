package net.sydokiddo.chrysalis.client.particles.types;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class GlowingSporeParticle extends FadingEmissiveParticle implements ParticleCommonMethods {

    // region Initialization and Ticking

    public GlowingSporeParticle(ClientLevel clientLevel, SpriteSet spriteSet, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(clientLevel, x, y, z, 1.0F, 0.0F, spriteSet, false);
        this.setSize(0.001F, 0.001F);
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
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

    @Environment(EnvType.CLIENT)
    public static class GlowingSporeParticleProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public GlowingSporeParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new GlowingSporeParticle(clientLevel, this.spriteSet, x, y, z, velocityX, velocityY, velocityZ);
        }
    }

    // endregion
}