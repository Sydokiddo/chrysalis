package net.sydokiddo.chrysalis.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class FadingTrailParticle extends TextureSheetParticle {

    // region Initialization and Ticking

    public FadingTrailParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(clientLevel, x, y, z);
        this.quadSize *= 0.75F;
        this.lifetime = 35;
        this.gravity = 0.0F;
        this.xd = velocityX;
        this.yd = velocityY + (double) (this.random.nextFloat() / 500.0F);
        this.zd = velocityZ;
    }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ < this.lifetime && this.alpha > 0.0F) {

            this.xd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
            this.zd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
            this.yd -= this.gravity;

            this.move(this.xd, this.yd, this.zd);
            if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) this.alpha -= 0.015F;

        } else {
            this.remove();
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
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickRate) {
        this.alpha = 1.0F - Mth.clamp(((float) this.age + tickRate) / (float) this.lifetime, 0.0F, 1.0F);
        super.render(vertexConsumer, camera, tickRate);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // endregion

    // region Providers

    @Environment(EnvType.CLIENT)
    public static class FadingTrailParticleProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public FadingTrailParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            FadingTrailParticle particle = new FadingTrailParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ);
            particle.setAlpha(1.0F);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    // endregion
}