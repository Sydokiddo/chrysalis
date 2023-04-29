package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class GlowingSporeParticle extends TextureSheetParticle {

    private GlowingSporeParticle(ClientLevel clientLevel, SpriteSet spriteSet, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(clientLevel, x, y, z);
        this.setSize(0.001F, 0.001F);
        this.setColor(0.5f, 0.5f, 0.5f);
        this.pickSprite(spriteSet);
        this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
        this.hasPhysics = true;
        xd = velocityX;
        yd = velocityY;
        zd = velocityZ;
    }

    @Override
    public void tick() {
        if (age > (lifetime / 2)) {
            if (alpha > 0.1F) {
                alpha -= 0.015F;
            } else {
                remove();
            }
        }
        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float f) {
        float g = ((float)this.age + f) / (float)this.lifetime;
        return this.quadSize * (1.0f - g * g * 0.5f);
    }

    @Override
    public int getLightColor(float f) {
        float g = ((float)this.age + f) / (float)this.lifetime;
        g = Mth.clamp(g, 1.0f, 0.0f);
        int i = super.getLightColor(f);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int)(g * 15.0f * 16.0f)) > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Environment(EnvType.CLIENT)
    public static class ModParticleProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public ModParticleProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel clientLevel, double x, double y, double z, double vx, double vy, double vz) {
            return new GlowingSporeParticle(clientLevel, sprite, x, y, z, vx, vy, vz);
        }
    }
}