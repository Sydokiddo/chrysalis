package net.junebug.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class RotatingGravityParticle extends RisingParticle {

    /**
     * A particle that rotates and either falls or rises, with custom physics for colliding with a block.
     **/

    // region Initialization and Ticking

    private final boolean hasGravity;

    public RotatingGravityParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, boolean hasGravity) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.hasGravity = hasGravity;
        this.scale(1.1F + (float) this.random.nextInt(6) / 10.0F);
        this.lifetime = (int) (8.0D / (this.random.nextDouble() * 0.8D + 0.2D)) + 12;
        this.roll = this.oRoll = this.random.nextFloat() * (float) (2.0F * Math.PI);
        this.yd = hasGravity ? -0.25D : 0.25D;
    }

    @Override
    public void tick() {

        super.tick();
        if (this.age > this.lifetime / 2) this.setAlpha(1.0F - Mth.clamp((float) this.age / (float) this.lifetime, 0.0F, 1.0F));

        if (this.age == 1) {
            this.xd = this.xd + (this.random.nextDouble() * 2.0D - 1.0D) * 0.2D;
            this.yd = 0.3D + (double) this.level.getRandom().nextInt(11) / 100.0D;
            this.zd = this.zd + (this.random.nextDouble() * 2.0D - 1.0D) * 0.2D;
        } else if (this.age <= 10 && this.hasGravity) {
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

    @SuppressWarnings("unused")
    @OnlyIn(Dist.CLIENT)
    public static class FallingProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public FallingProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RotatingGravityParticle particle = new RotatingGravityParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, true);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    @SuppressWarnings("unused")
    @OnlyIn(Dist.CLIENT)
    public static class RisingProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public RisingProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RotatingGravityParticle particle = new RotatingGravityParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, false);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    // endregion
}