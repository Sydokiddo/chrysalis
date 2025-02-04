package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class RotatingFallingParticle extends RisingParticle {

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

        if (this.age > this.lifetime / 2) {
            this.setAlpha(1.0F - ((float) this.age - (float) (this.lifetime / 2)) / (float) this.lifetime);
        }

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

    @Environment(EnvType.CLIENT)
    public static class RotatingFallingParticleProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public RotatingFallingParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            RotatingFallingParticle particle = new RotatingFallingParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    // endregion
}