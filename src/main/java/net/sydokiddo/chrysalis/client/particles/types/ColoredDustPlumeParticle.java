package net.sydokiddo.chrysalis.client.particles.types;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.client.particles.options.ColoredDustPlumeParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class ColoredDustPlumeParticle extends DustPlumeParticle implements ParticleCommonMethods {

    /**
     * A colored variant of the vanilla dust plume particles, with an option to make it emissive.
     **/

    // region Initialization

    private final boolean emissive;

    public ColoredDustPlumeParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ, ColoredDustPlumeParticleOptions particleOptions, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, velocityX, velocityY, velocityZ, particleOptions.getScale(), spriteSet);

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

    // endregion

    // region Rendering

    @Override
    public int getLightColor(float tickRate) {
        if (this.emissive) return this.fadeLightColor(1.0F, 0.0F, this.age, this.lifetime, super.getLightColor(tickRate));
        return super.getLightColor(tickRate);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // endregion

    // region Providers

    @Environment(EnvType.CLIENT)
    public static class ColoredDustPlumeParticleProvider implements ParticleProvider<ColoredDustPlumeParticleOptions> {

        private final SpriteSet spriteSet;

        public ColoredDustPlumeParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull ColoredDustPlumeParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new ColoredDustPlumeParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ, particleOptions, this.spriteSet);
        }
    }

    // endregion
}