package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class GlowingSporeParticle extends TextureSheetParticle {

    // region Initialization and Ticking

    public GlowingSporeParticle(ClientLevel clientLevel, SpriteSet spriteSet, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(clientLevel, x, y, z);
        this.setSize(0.001F, 0.001F);
        this.setColor(0.5F, 0.5F, 0.5F);
        this.lifetime = (int) (8.0 / (Math.random() * 0.8 + 0.2)) + 4;
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

        float divider = ((float) this.age) / (float) this.lifetime;
        divider = Mth.clamp(divider, 1.0F, 0.0F);

        int lightColor = super.getLightColor(tickRate);
        int int1 = lightColor & 0xFF;
        int int2 = lightColor >> 16 & 0xFF;

        if ((int1 += (int) (divider * 15.0F * 16.0F)) > 240) {
            int1 = 240;
        }

        return int1 | int2 << 16;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // endregion

    // region Providers

    @Environment(EnvType.CLIENT)
    public static class GlowingSporeProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public GlowingSporeProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new GlowingSporeParticle(clientLevel, this.spriteSet, x, y, z, velocityX, velocityY, velocityZ);
        }
    }

    // endregion
}