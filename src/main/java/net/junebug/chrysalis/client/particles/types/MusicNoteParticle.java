package net.junebug.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.junebug.chrysalis.client.particles.ParticleCommonMethods;
import net.junebug.chrysalis.client.particles.options.MusicNoteParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class MusicNoteParticle extends NoteParticle implements ParticleCommonMethods {

    /**
     * A rising music note particle that rotates over time, with options to configure the color and scale.
     **/

    // region Initialization and Ticking

    private final boolean
        reverse,
        emissive
    ;

    public MusicNoteParticle(ClientLevel clientLevel, double x, double y, double z, MusicNoteParticleOptions particleOptions, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, particleOptions.getColor());

        this.pickSprite(spriteSet);
        this.scale(particleOptions.getScale());
        this.reverse = this.random.nextBoolean();
        this.lifetime = 16;

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

        super.tick();

        if (!this.removed) {
            this.oRoll = this.roll;
            this.roll = this.reverse ? this.roll - 0.025F : this.roll + 0.025F;
            this.move(this.xd, this.yd, this.zd);
        }

        if (this.age > this.lifetime / 2) this.setAlpha(1.0F - Mth.clamp((float) this.age / (float) this.lifetime, 0.0F, 1.0F));
    }

    // endregion

    // region Rendering

    @Override
    public int getLightColor(float tickRate) {
        if (this.emissive) return this.fadeLightColor(1.0F, 0.0F, this.age, this.lifetime, super.getLightColor(tickRate));
        return super.getLightColor(tickRate);
    }

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
    public static class Provider implements ParticleProvider<MusicNoteParticleOptions> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull MusicNoteParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new MusicNoteParticle(clientLevel, x, y, z, particleOptions, this.spriteSet);
        }
    }

    // endregion
}