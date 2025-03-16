package net.sydokiddo.chrysalis.client.particles.types;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.client.particles.options.ColoredPortalParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class ColoredPortalParticle extends TextureSheetParticle implements ParticleCommonMethods {

    /**
     * A more customizable version of the vanilla portal particles with configurable colors and properties.
     **/

    // region Initialization and Ticking

    private final ColoredPortalParticleOptions particleOptions;

    private final double xStart;
    private final double yStart;
    private final double zStart;

    private final Vector3f startingColor;
    private final Vector3f endingColor;

    public ColoredPortalParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @NotNull ColoredPortalParticleOptions particleOptions) {
        super(clientLevel, x, y, z);

        this.particleOptions = particleOptions;

        this.xStart = this.x;
        this.yStart = this.y;
        this.zStart = this.z;

        this.x = x;
        this.y = y;
        this.z = z;
        this.xd = velocityX;
        this.yd = velocityY;
        this.zd = velocityZ;

        this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
        this.lifetime = (int) (Math.random() * 10.0D) + 40;

        if (particleOptions.isReverse()) {
            this.quadSize *= 1.5F;
            this.lifetime = (int) (Math.random() * 2.0D) + 60;
        }

        this.startingColor = particleOptions.getFinalStartingColor();
        this.endingColor = particleOptions.getFinalEndingColor();
    }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {

            float math = (float) this.age / (float) this.lifetime;

            if (this.particleOptions.isReverse()) {
                this.x += this.xd * (double) math;
                this.y += this.yd * (double) math;
                this.z += this.zd * (double) math;
            } else {
                float multiplier = 1.0F - (-math + math * math * 2.0F);
                this.x = this.xStart + this.xd * (double) multiplier;
                this.y = this.yStart + this.yd * (double) multiplier + (double) (1.0F - math);
                this.z = this.zStart + this.zd * (double) multiplier;
            }

            this.setPos(this.x, this.y, this.z);
        }
    }

    // endregion

    // region Rendering

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(float scaleFactor) {

        float math;

        if (this.particleOptions.isReverse()) {
            math = 1.0F - ((float) this.age + scaleFactor) / ((float) this.lifetime * 1.5F);
        } else {
            math = ((float) this.age + scaleFactor) / (float) this.lifetime;
            math = 1.0F - math;
            math *= math;
            math = 1.0F - math;
        }

        return this.quadSize * math;
    }

    @Override
    public int getLightColor(float tickRate) {

        int original = super.getLightColor(tickRate);

        float math1 = (float) this.age / (float) this.lifetime;
        math1 *= math1;
        math1 *= math1;

        int int1 = original & 255;
        int int2 = original >> 16 & 255;
        int2 += (int) (math1 * 15.0F * 16.0F);
        if (int2 > 240) int2 = 240;

        return int1 | int2 << 16;
    }

    @Override
    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float tickRate) {

        Vector3f vector3f = new Vector3f(this.startingColor).lerp(this.endingColor, ((float) this.age + tickRate) / ((float) this.lifetime + 1.0F));
        this.rCol = vector3f.x();
        this.gCol = vector3f.y();
        this.bCol = vector3f.z();

        super.render(vertexConsumer, camera, tickRate);
    }

    // endregion

    // region Providers

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ColoredPortalParticleOptions> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull ColoredPortalParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            ColoredPortalParticle coloredPortalParticle = new ColoredPortalParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ, particleOptions);
            coloredPortalParticle.pickSprite(this.spriteSet);
            return coloredPortalParticle;
        }
    }

    // endregion
}