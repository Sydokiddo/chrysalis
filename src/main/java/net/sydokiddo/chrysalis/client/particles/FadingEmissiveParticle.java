package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class FadingEmissiveParticle extends TextureSheetParticle {

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

        float divider = ((float) this.age) / (float) this.lifetime;
        divider = Mth.clamp(divider, this.startingBrightness, this.endingBrightness);

        int lightColor = super.getLightColor(tickRate);
        int int1 = lightColor & 0xFF;
        int int2 = lightColor >> 16 & 0xFF;

        if ((int1 += (int) (divider * 15.0F * 16.0F)) > 240) {
            int1 = 240;
        }

        return int1 | int2 << 16;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // endregion
}