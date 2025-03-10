package net.sydokiddo.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class FadingTrailParticle extends TextureSheetParticle implements ParticleCommonMethods {

    /**
     * A fading dust trail particle effect.
     **/

    // region Initialization and Ticking

    public FadingTrailParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(clientLevel, x, y, z);
        this.quadSize *= 0.75F;
        this.lifetime = 40;
        this.gravity = 0.0F;
        this.xd = velocityX;
        this.yd = velocityY + (double) (this.random.nextFloat() / 500.0F);
        this.zd = velocityZ;
    }

    @Override
    public void tick() {

        super.tick();

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age > this.lifetime / 2) this.setAlpha(1.0F - Mth.clamp((float) this.age / (float) this.lifetime, 0.0F, 1.0F));

        if (this.age < this.lifetime && this.alpha > 0.0F) {
            this.xd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
            this.yd -= this.gravity;
            this.zd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
            this.move(this.xd, this.yd, this.zd);
        }
    }

    // endregion

    // region Rendering

    @Override
    public float getQuadSize(float tickRate) {
        return this.shrinkParticle(this.quadSize, ((float) this.age + tickRate) / (float) this.lifetime);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            FadingTrailParticle particle = new FadingTrailParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ);
            particle.setAlpha(1.0F);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    // endregion
}