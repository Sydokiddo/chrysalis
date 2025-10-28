package net.junebug.chrysalis.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.junebug.chrysalis.client.particles.ParticleCommonMethods;
import net.junebug.chrysalis.common.misc.CParticles;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class ColoredDustPlumeParticleOptions extends ScalableParticleOptionsBase implements ParticleCommonMethods {

    /**
     * The options class for colored dust plume particles.
     **/

    public static final MapCodec<ColoredDustPlumeParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.colorString, Color.LIGHT_GRAY.getRGB()).forGetter(ParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.randomizeColorString, false).forGetter(ParticleCommonMethods::shouldRandomizeColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.emissiveString, false).forGetter(ParticleCommonMethods::isEmissive),
        SCALE.optionalFieldOf(ParticleCommonMethods.scaleString, 1.0F).forGetter(ScalableParticleOptionsBase::getScale))
    .apply(instance, ColoredDustPlumeParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColoredDustPlumeParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::isEmissive,
        ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale,
        ColoredDustPlumeParticleOptions::new
    );

    private final int color;

    private final boolean
        randomizeColor,
        emissive
    ;

    public ColoredDustPlumeParticleOptions(int color, boolean randomizeColor, boolean emissive, float scale) {
        super(scale);
        this.color = color;
        this.randomizeColor = randomizeColor;
        this.emissive = emissive;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public boolean shouldRandomizeColor() {
        return this.randomizeColor;
    }

    @Override
    public boolean isEmissive() {
        return this.emissive;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return CParticles.COLORED_DUST_PLUME.get();
    }
}