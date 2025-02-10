package net.sydokiddo.chrysalis.client.particles.types;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.client.particles.options.RisingDustParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class RisingDustParticle extends FadingEmissiveParticle implements ParticleCommonMethods {

    // region Initialization and Ticking

    public RisingDustParticle(ClientLevel clientLevel, double x, double y, double z, RisingDustParticleOptions particleOptions, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, particleOptions.getStartingBrightness(), particleOptions.getEndingBrightness(), spriteSet, false);

        this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
        this.hasPhysics = false;
        this.roll = this.oRoll = this.random.nextFloat() * (float) (2.0F * Math.PI);
        this.yd = this.yd * 0.01F + 0.025F;

        this.scale(particleOptions.getScale() * 4.0F);
        this.pickSprite(spriteSet);

        Vector3f color = particleOptions.getFinalColor();

        if (particleOptions.shouldRandomizeColor()) {
            this.rCol = particleOptions.randomizeColor(color.x(), this.random);
            this.gCol = particleOptions.randomizeColor(color.y(), this.random);
            this.bCol = particleOptions.randomizeColor(color.z(), this.random);
        } else {
            this.rCol = color.x();
            this.gCol = color.y();
            this.bCol = color.z();
        }
    }

    @Override
    public void tick() {

        super.tick();

        if (!this.removed) {
            this.oRoll = this.roll;
            this.roll = this.roll + 0.1F;
            this.move(this.xd, this.yd, this.zd);
        }
    }

    // endregion

    // region Rendering

    @Override
    public float getQuadSize(float tickRate) {
        return this.growParticle(this.quadSize, this.age, tickRate, this.lifetime);
    }

    // endregion

    // region Providers

    @Environment(EnvType.CLIENT)
    public static class RisingDustParticleProvider implements ParticleProvider<RisingDustParticleOptions> {

        private final SpriteSet spriteSet;

        public RisingDustParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull RisingDustParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new RisingDustParticle(clientLevel, x, y, z, particleOptions, this.spriteSet);
        }
    }

    // endregion
}