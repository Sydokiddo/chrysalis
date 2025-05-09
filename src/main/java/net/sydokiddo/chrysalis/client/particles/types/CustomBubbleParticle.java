package net.sydokiddo.chrysalis.client.particles.types;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CustomBubbleParticle extends BubbleParticle implements ParticleCommonMethods {

    // region Initialization and Ticking

    private final TagKey<Fluid> fluidType;
    private final boolean emissive;

    public CustomBubbleParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ, TagKey<Fluid> fluidType, boolean emissive) {
        super(clientLevel, x, y, z, velocityX, velocityY, velocityZ);
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

            this.yd += 0.002;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.8500000238418579;
            this.yd *= 0.8500000238418579;
            this.zd *= 0.8500000238418579;

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
        public Particle createParticle(@NotNull SimpleParticleType particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            CustomBubbleParticle particle = new CustomBubbleParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ, FluidTags.WATER, false);
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
        public Particle createParticle(@NotNull SimpleParticleType particleOptions, @NotNull ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            CustomBubbleParticle particle = new CustomBubbleParticle(clientLevel, x, y, z, velocityX, velocityY, velocityZ, FluidTags.LAVA, true);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }

    // endregion
}