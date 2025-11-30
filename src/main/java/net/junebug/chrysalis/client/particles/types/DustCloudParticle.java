package net.junebug.chrysalis.client.particles.types;

import net.junebug.chrysalis.client.particles.ParticleCommonMethods;
import net.junebug.chrysalis.client.particles.options.DustCloudParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class DustCloudParticle extends CampfireSmokeParticle implements ParticleCommonMethods {

    /**
     * An animated dust cloud particle that can have its color configured.
     **/

    // region Initialization and Ticking

    private final SpriteSet spriteSet;
    private final boolean emissive;

    public DustCloudParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, DustCloudParticleOptions particleOptions, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, false);

        this.lifetime = 10;
        this.spriteSet = spriteSet;
        this.setSpriteFromAge(spriteSet);
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

        this.emissive = particleOptions.isEmissive();
    }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ < this.lifetime && this.alpha > 0.0F) {
            this.xd += this.random.nextFloat() / 5000.0F * (this.random.nextBoolean() ? 1.0F : -1.0F);
            this.yd -= this.gravity;
            this.zd += this.random.nextFloat() / 5000.0F * (this.random.nextBoolean() ? 1.0F : -1.0F);
            this.move(this.xd, this.yd, this.zd);
            this.setSpriteFromAge(this.spriteSet);
        } else {
            this.remove();
        }

        if (this.age > this.lifetime) this.setAlpha(1.0F - Mth.clamp((float) this.age / (float) this.lifetime, 0.0F, 1.0F));
    }

    @Override
    public int getLightColor(float tickRate) {
        if (this.emissive) return this.fadeLightColor(1.0F, 0.0F, this.age, this.lifetime, super.getLightColor(tickRate));
        return super.getLightColor(tickRate);
    }

    // endregion

    // region Providers

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<DustCloudParticleOptions> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull DustCloudParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DustCloudParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, particleOptions, this.spriteSet);
        }
    }

    // endregion
}