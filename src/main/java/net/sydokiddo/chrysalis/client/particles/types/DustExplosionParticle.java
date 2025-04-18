package net.sydokiddo.chrysalis.client.particles.types;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.client.particles.options.DustExplosionParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class DustExplosionParticle extends ExplodeParticle implements ParticleCommonMethods {

    /**
     * A fading dust cloud explosion particle effect, with options to set the starting color, the color to fade into, and whether the particle is emissive.
     **/

    // region Initialization and Ticking

    private final Vector3f
        startingColor,
        endingColor
    ;

    private final boolean emissive;

    public DustExplosionParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ, DustExplosionParticleOptions particleOptions, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, velocityX, velocityY, velocityZ, spriteSet);
        this.gravity = 0.5F;
        this.startingColor = particleOptions.getFinalStartingColor();
        this.endingColor = particleOptions.getFinalEndingColor();
        this.emissive = particleOptions.isEmissive();
        this.scale(particleOptions.getScale());
    }

    @Override
    public void tick() {
        super.tick();
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
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float tickRate) {

        Vector3f vector3f = new Vector3f(this.startingColor).lerp(this.endingColor, ((float) this.age + tickRate) / ((float) this.lifetime + 1.0F) / 1.5F);
        this.rCol = vector3f.x();
        this.gCol = vector3f.y();
        this.bCol = vector3f.z();

        super.render(vertexConsumer, camera, tickRate);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // endregion

    // region Providers

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<DustExplosionParticleOptions> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull DustExplosionParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new DustExplosionParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ, particleOptions, this.spriteSet);
        }
    }

    // endregion
}