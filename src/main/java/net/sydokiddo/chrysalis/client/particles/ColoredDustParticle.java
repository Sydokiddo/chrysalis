package net.sydokiddo.chrysalis.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class ColoredDustParticle extends DustPlumeParticle {

    // region Initialization

    private final boolean isEmissive;

    public ColoredDustParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float size, SpriteSet spriteSet, float red, float green, float blue, boolean emissive) {
        super(clientLevel, x, y, z, velocityX, velocityY, velocityZ, size, spriteSet);
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.isEmissive = emissive;
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
}