package net.sydokiddo.chrysalis.client.particles.types;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class FadingEmissiveParticle extends TextureSheetParticle implements ParticleCommonMethods {

    // region Initialization and Ticking

    private final SpriteSet spriteSet;
    private final float startingBrightness;
    private final float endingBrightness;
    private final boolean animateTextures;

    public FadingEmissiveParticle(ClientLevel clientLevel, double x, double y, double z, float startingBrightness, float endingBrightness, SpriteSet spriteSet, boolean animateTextures) {
        super(clientLevel, x, y, z);
        this.spriteSet = spriteSet;
        this.startingBrightness = startingBrightness;
        this.endingBrightness = endingBrightness;
        this.animateTextures = animateTextures;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.animateTextures) this.setSpriteFromAge(this.spriteSet);
        if (this.age > this.lifetime / 2) this.setAlpha(1.0F - Mth.clamp((float) this.age / (float) this.lifetime, 0.0F, 1.0F));
    }

    // endregion

    // region Rendering

    @Override
    public int getLightColor(float tickRate) {
        return this.fadeLightColor(this.startingBrightness, this.endingBrightness, this.age, this.lifetime, super.getLightColor(tickRate));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // endregion
}