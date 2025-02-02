package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.sydokiddo.chrysalis.client.particles.options.ColoredDustPlumeParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class ColoredDustPlumeParticle extends DustPlumeParticle {

    // region Initialization

    private final boolean isEmissive;

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

        this.isEmissive = particleOptions.isEmissive();
    }

    // endregion

    // region Rendering

    @Override
    public int getLightColor(float tickRate) {

        if (this.isEmissive) {

            float divider = ((float) this.age) / (float) this.lifetime;
            divider = Mth.clamp(divider, 1.0F, 0.0F);

            int lightColor = super.getLightColor(tickRate);
            int int1 = lightColor & 0xFF;
            int int2 = lightColor >> 16 & 0xFF;

            if ((int1 += (int) (divider * 15.0F * 16.0F)) > 240) {
                int1 = 240;
            }

            return int1 | int2 << 16;
        }

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