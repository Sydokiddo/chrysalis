package net.sydokiddo.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class RotatingFallingParticle extends RisingParticle {

    /**
     * A particle effect that rotates and falls, with custom physics for colliding with a block.
     **/

    // region Initialization and Ticking

    public RotatingFallingParticle(ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(level, x, y, z, velocityX, velocityY, velocityZ);
        this.scale(1.1F + (float) this.random.nextInt(6) / 10.0F);
        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 12;
        this.roll = this.oRoll = this.random.nextFloat() * (float) (2.0F * Math.PI);
        this.yd = -0.25D;
    }

    @Override
    public void tick() {

        super.tick();
        if (this.age > this.lifetime / 2) this.setAlpha(1.0F - Mth.clamp((float) this.age / (float) this.lifetime, 0.0F, 1.0F));

        if (this.age == 1) {
            this.xd = this.xd + (Math.random() * 2.0D - 1.0D) * 0.2D;
            this.yd = 0.3D + (double) this.level.getRandom().nextInt(11) / 100.0D;
            this.zd = this.zd + (Math.random() * 2.0D - 1.0D) * 0.2D;
        } else if (this.age <= 10) {
            this.yd = this.yd - (0.05D + (double) this.age / 200.0D);
        }

        if (this.onGround) {
            this.setParticleSpeed(0.0D, 0.0D, 0.0D);
            this.setPos(this.xo, this.yo + 0.1D, this.zo);
        }
    }

    // endregion

    // region Rendering

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
            RotatingFallingParticle particle = new RotatingFallingParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    // endregion
}