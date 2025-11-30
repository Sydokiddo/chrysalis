package net.junebug.chrysalis.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.junebug.chrysalis.client.particles.ParticleCommonMethods;
import net.junebug.chrysalis.common.misc.CParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class DustCloudParticleOptions implements ParticleOptions, ParticleCommonMethods {

    /**
     * The options class for dust cloud particles.
     **/

    public static final int defaultColor = Color.decode("#685C53").getRGB();

    public static final MapCodec<DustCloudParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.colorString, defaultColor).forGetter(ParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.randomizeColorString, false).forGetter(ParticleCommonMethods::shouldRandomizeColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.emissiveString, false).forGetter(ParticleCommonMethods::isEmissive))
    .apply(instance, DustCloudParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DustCloudParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::isEmissive,
        DustCloudParticleOptions::new
    );

    private final int color;

    private final boolean
        randomizeColor,
        emissive
    ;

    public DustCloudParticleOptions(int color, boolean randomizeColor, boolean emissive) {
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
        return CParticles.DUST_CLOUD.get();
    }
}