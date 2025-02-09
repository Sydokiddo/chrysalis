package net.sydokiddo.chrysalis.client.particles.types;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class GlowingSporeParticle extends TextureSheetParticle implements ParticleCommonMethods {

    // region Initialization and Ticking

    public GlowingSporeParticle(ClientLevel clientLevel, SpriteSet spriteSet, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(clientLevel, x, y, z);
        this.setSize(0.001F, 0.001F);
        this.setColor(0.5F, 0.5F, 0.5F);
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.hasPhysics = true;
        this.xd = velocityX;
        this.yd = velocityY;
        this.zd = velocityZ;
        this.pickSprite(spriteSet);
    }

    @Override
    public void tick() {

        super.tick();

        if (this.age > (this.lifetime / 2)) {
            if (this.alpha > 0.1F) this.alpha -= 0.015F;
            else this.remove();
        }
    }

    // endregion

    // region Rendering

    @Override
    public float getQuadSize(float tickRate) {
        float divider = ((float) this.age + tickRate) / (float) this.lifetime;
        return this.quadSize * (1.0F - divider * divider * 0.5F);
    }

    @Override
    public int getLightColor(float tickRate) {
        return this.fadeLightColor(1.0F, 0.0F, this.age, this.lifetime, super.getLightColor(tickRate));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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