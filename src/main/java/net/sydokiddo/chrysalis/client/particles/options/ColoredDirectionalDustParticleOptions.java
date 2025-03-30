package net.sydokiddo.chrysalis.client.particles.options;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.common.misc.CParticles;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public record ColoredDirectionalDustParticleOptions(int startingColor, int endingColor) implements ParticleOptions, ParticleCommonMethods {

    /**
     * The options class for colored directional dust particles.
     **/

    public static final MapCodec<ColoredDirectionalDustParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.startingColorString, Color.LIGHT_GRAY.getRGB()).forGetter(ColoredDirectionalDustParticleOptions::startingColor),
        ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.endingColorString, Color.LIGHT_GRAY.getRGB()).forGetter(ColoredDirectionalDustParticleOptions::endingColor))
        .apply(instance, ColoredDirectionalDustParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColoredDirectionalDustParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ColoredDirectionalDustParticleOptions::startingColor,
        ByteBufCodecs.INT, ColoredDirectionalDustParticleOptions::endingColor,
        ColoredDirectionalDustParticleOptions::new
    );

    @Override
    public @NotNull ParticleType<?> getType() {
        return CParticles.COLORED_DIRECTIONAL_DUST.get();
    }
}