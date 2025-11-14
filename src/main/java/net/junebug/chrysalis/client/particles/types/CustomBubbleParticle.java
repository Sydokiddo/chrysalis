package net.junebug.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.junebug.chrysalis.client.particles.ParticleCommonMethods;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CustomBubbleParticle extends BubbleParticle implements ParticleCommonMethods {

    /**
     * Vanilla's bubble particles with an option to make it tied to a specific fluid type as well as making it emissive.
     **/

    // region Initialization and Ticking

    private final TagKey<Fluid> fluidType;
    private final boolean emissive;

    public CustomBubbleParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TagKey<Fluid> fluidType, boolean emissive) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.fluidType = fluidType;
        this.emissive = emissive;
    }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.lifetime-- <= 0) {
            this.remove();
        } else {

            this.yd += 0.002D;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.85D;
            this.yd *= 0.85D;
            this.zd *= 0.85D;

            if (!this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(this.fluidType)) this.remove();
        }
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

    @SuppressWarnings("unused")
    @OnlyIn(Dist.CLIENT)
    public static class WaterProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public WaterProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CustomBubbleParticle particle = new CustomBubbleParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, FluidTags.WATER, false);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class LavaProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public LavaProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CustomBubbleParticle particle = new CustomBubbleParticle(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, FluidTags.LAVA, true);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    // endregion
}