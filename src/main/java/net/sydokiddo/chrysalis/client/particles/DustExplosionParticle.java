package net.sydokiddo.chrysalis.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.sydokiddo.chrysalis.client.particles.options.DustExplosionParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class DustExplosionParticle extends ExplodeParticle {

    // region Initialization and Ticking

    private final Vector3f startingColor;
    private final Vector3f endingColor;
    private final boolean emissive;

    public DustExplosionParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ, DustExplosionParticleOptions particleOptions, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, velocityX, velocityY, velocityZ, spriteSet);
        this.gravity = 0.5F;
        this.startingColor = particleOptions.getFinalStartingColor();
        this.endingColor = particleOptions.getFinalEndingColor();
        this.emissive = particleOptions.isEmissive();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.alpha > 0.25F) this.alpha -= 0.0075F;
        else this.remove();
    }

    // endregion

    // region Rendering

    @Override
    public int getLightColor(float tickRate) {

        if (this.emissive) {
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
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickRate) {

        Vector3f vector3f = new Vector3f(this.startingColor).lerp(this.endingColor, ((float) this.age + tickRate) / ((float) this.lifetime + 1.0F));
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

    @Environment(EnvType.CLIENT)
    public static class DustExplosionParticleProvider implements ParticleProvider<DustExplosionParticleOptions> {

        private final SpriteSet spriteSet;

        public DustExplosionParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull DustExplosionParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new DustExplosionParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ, particleOptions, this.spriteSet);
        }
    }

    // endregion
}