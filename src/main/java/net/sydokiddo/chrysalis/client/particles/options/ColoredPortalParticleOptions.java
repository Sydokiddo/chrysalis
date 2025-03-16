package net.sydokiddo.chrysalis.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.common.misc.ChrysalisParticles;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class ColoredPortalParticleOptions implements ParticleOptions, ParticleCommonMethods {

    /**
     * The options class for colored portal particles.
     **/

    public static final MapCodec<ColoredPortalParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.startingColorString, Color.LIGHT_GRAY.getRGB()).forGetter(ColoredPortalParticleOptions::getStartingColor),
        ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.endingColorString, Color.LIGHT_GRAY.getRGB()).forGetter(ColoredPortalParticleOptions::getEndingColor),
        Codec.BOOL.optionalFieldOf("reverse", false).forGetter(ColoredPortalParticleOptions::isReverse))
    .apply(instance, ColoredPortalParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColoredPortalParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ColoredPortalParticleOptions::getStartingColor,
        ByteBufCodecs.INT, ColoredPortalParticleOptions::getEndingColor,
        ByteBufCodecs.BOOL, ColoredPortalParticleOptions::isReverse,
        ColoredPortalParticleOptions::new
    );

    private final int startingColor;
    private final int endingColor;
    private final boolean reverse;

    public ColoredPortalParticleOptions(int startingColor, int endingColor, boolean reverse) {
        this.startingColor = startingColor;
        this.endingColor = endingColor;
        this.reverse = reverse;
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

    public boolean isReverse() {
        return this.reverse;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return ChrysalisParticles.COLORED_PORTAL.get();
    }
}