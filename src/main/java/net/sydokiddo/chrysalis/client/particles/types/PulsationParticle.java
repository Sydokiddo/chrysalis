package net.sydokiddo.chrysalis.client.particles.types;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.client.particles.options.LargePulsationParticleOptions;
import net.sydokiddo.chrysalis.client.particles.options.SmallPulsationParticleOptions;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class PulsationParticle extends FadingEmissiveParticle {

    /**
     * A directional pulsation ring particle effect with the ability to configure the color.
     **/

    // region Initialization and Ticking

    private final Direction direction;

    public PulsationParticle(ClientLevel clientLevel, double x, double y, double z, SmallPulsationParticleOptions particleOptions, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, 1.0F, 0.0F, spriteSet, true);

        this.lifetime = particleOptions.getLifeTime();
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);
        this.direction = particleOptions.getFinalDirection();

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

    // endregion

    // region Rendering

    @Override
    public float getQuadSize(float tickRate) {
        return this.growParticle(this.quadSize, this.age, tickRate, this.lifetime);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float tickRate) {

        Vec3 cameraPosition = camera.getPosition();
        float x = (float) (Mth.lerp(tickRate, this.xo, this.x) - cameraPosition.x());
        float y = (float) (Mth.lerp(tickRate, this.yo, this.y) - cameraPosition.y());
        float z = (float) (Mth.lerp(tickRate, this.zo, this.z) - cameraPosition.z());

        Quaternionf quaternion = this.direction.getRotation();
        quaternion.mul(Axis.XP.rotationDegrees(90.0F));
        quaternion.mul(Axis.ZP.rotation(Mth.lerp(tickRate, this.oRoll, this.roll)));

        new Vector3f(-1.0F, -1.0F, 0.0F).rotate(quaternion);
        Vector3f[] faces = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };

        for (int rotation = 0; rotation < 4; ++rotation) {
            Vector3f face = faces[rotation];
            face.rotate(quaternion);
            face.mul(this.getQuadSize(tickRate));
            float addedSpace = this.direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 0.01F : -0.01F;
            face.add(x + addedSpace, y + addedSpace, z + addedSpace);
        }

        int lightColor = this.getLightColor(tickRate);
        vertexConsumer.addVertex(faces[0].x(), faces[0].y(), faces[0].z()).setUv(this.getU1(), this.getV1()).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lightColor);
        vertexConsumer.addVertex(faces[1].x(), faces[1].y(), faces[1].z()).setUv(this.getU1(), this.getV0()).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lightColor);
        vertexConsumer.addVertex(faces[2].x(), faces[2].y(), faces[2].z()).setUv(this.getU0(), this.getV0()).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lightColor);
        vertexConsumer.addVertex(faces[3].x(), faces[3].y(), faces[3].z()).setUv(this.getU0(), this.getV1()).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(lightColor);
    }

    // endregion

    // region Providers

    @Environment(EnvType.CLIENT)
    public static class SmallPulsationParticleProvider implements ParticleProvider<SmallPulsationParticleOptions> {

        private final SpriteSet spriteSet;

        public SmallPulsationParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SmallPulsationParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            PulsationParticle particle = new PulsationParticle(clientLevel, x, y, z, particleOptions, this.spriteSet);
            particle.scale(8.0F);
            return particle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class LargePulsationParticleProvider implements ParticleProvider<LargePulsationParticleOptions> {

        private final SpriteSet spriteSet;

        public LargePulsationParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull LargePulsationParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            PulsationParticle particle = new PulsationParticle(clientLevel, x, y, z, particleOptions, this.spriteSet);
            particle.scale(32.0F);
            return particle;
        }
    }

    // endregion
}