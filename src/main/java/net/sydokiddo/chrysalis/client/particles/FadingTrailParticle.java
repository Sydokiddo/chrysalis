package net.sydokiddo.chrysalis.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

@SuppressWarnings("ALL")
@Environment(EnvType.CLIENT)
public class FadingTrailParticle extends TextureSheetParticle {

    private FadingTrailParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(clientLevel, d, e, f);
        this.quadSize *= 0.75f;
        this.lifetime = 30;
        this.gravity = 3.0E-6f;
        this.xd = g;
        this.yd = h + (double)(this.random.nextFloat() / 500.0f);
        this.zd = i;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float f) {
        this.alpha = 1.0F - Mth.clamp(((float)this.age + f) / (float)this.lifetime, 0.0F, 1.0F);
        super.render(vertexConsumer, camera, f);
    }

    @Override
    public float getQuadSize(float f) {
        float g = ((float)this.age + f) / (float)this.lifetime;
        return this.quadSize * (1.0f - g * g * 0.5f);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime && !(this.alpha <= 0.0F)) {
            this.xd += this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1);
            this.zd += this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1);
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
                this.alpha -= 0.015F;
            }
        } else {
            this.remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class ModParticleProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public ModParticleProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            FadingTrailParticle particle = new FadingTrailParticle(clientLevel, d, e, f, g, h, i);
            particle.setAlpha(1.0f);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}