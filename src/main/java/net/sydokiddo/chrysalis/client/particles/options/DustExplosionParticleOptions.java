package net.sydokiddo.chrysalis.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.common.misc.CParticles;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import java.awt.*;

public class DustExplosionParticleOptions extends ScalableParticleOptionsBase implements ParticleCommonMethods {

    /**
     * The options class for dust explosion particles.
     **/

    public static final MapCodec<DustExplosionParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.startingColorString, Color.LIGHT_GRAY.getRGB()).forGetter(DustExplosionParticleOptions::getStartingColor),
        ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.endingColorString, Color.LIGHT_GRAY.getRGB()).forGetter(DustExplosionParticleOptions::getEndingColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.emissiveString, false).forGetter(ParticleCommonMethods::isEmissive),
        SCALE.optionalFieldOf(ParticleCommonMethods.scaleString, 1.0F).forGetter(ScalableParticleOptionsBase::getScale))
    .apply(instance, DustExplosionParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DustExplosionParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, DustExplosionParticleOptions::getStartingColor,
        ByteBufCodecs.INT, DustExplosionParticleOptions::getEndingColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::isEmissive,
        ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale,
        DustExplosionParticleOptions::new
    );

    private final int
        startingColor,
        endingColor
    ;

    private final boolean emissive;

    public DustExplosionParticleOptions(int startingColor, int endingColor, boolean emissive, float scale) {
        super(scale);
        this.startingColor = startingColor;
        this.endingColor = endingColor;
        this.emissive = emissive;
    }

    private int getStartingColor() {
        return this.startingColor;
    }

    private int getEndingColor() {
        return this.endingColor;
    }

    public Vector3f getFinalStartingColor() {
        return ARGB.vector3fFromRGB24(this.startingColor);
    }

    public Vector3f getFinalEndingColor() {
        return ARGB.vector3fFromRGB24(this.endingColor);
    }

    @Override
    public boolean isEmissive() {
        return this.emissive;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return CParticles.DUST_EXPLOSION.get();
    }
}