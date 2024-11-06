package net.sydokiddo.chrysalis.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import java.awt.*;

@Environment(EnvType.CLIENT)
public class ColoredDustPlumeParticleOptions extends ScalableParticleOptionsBase {

    public static final MapCodec<ColoredDustPlumeParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", Color.LIGHT_GRAY.getRGB()).forGetter(coloredDustPlumeParticleOptions -> coloredDustPlumeParticleOptions.color),
        Codec.BOOL.optionalFieldOf("randomize_color", false).forGetter(coloredDustPlumeParticleOptions -> coloredDustPlumeParticleOptions.randomizeColor),
        Codec.BOOL.optionalFieldOf("emissive", false).forGetter(coloredDustPlumeParticleOptions -> coloredDustPlumeParticleOptions.emissive),
        SCALE.optionalFieldOf("scale", 1.0F).forGetter(ScalableParticleOptionsBase::getScale))
    .apply(instance, ColoredDustPlumeParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColoredDustPlumeParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, coloredDustPlumeParticleOptions -> coloredDustPlumeParticleOptions.color,
        ByteBufCodecs.BOOL, coloredDustPlumeParticleOptions -> coloredDustPlumeParticleOptions.randomizeColor,
        ByteBufCodecs.BOOL, coloredDustPlumeParticleOptions -> coloredDustPlumeParticleOptions.emissive,
        ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale,
        ColoredDustPlumeParticleOptions::new
    );

    private final int color;
    private final boolean randomizeColor;
    private final boolean emissive;

    public ColoredDustPlumeParticleOptions(int color, boolean randomizeColor, boolean emissive, float scale) {
        super(scale);
        this.color = color;
        this.randomizeColor = randomizeColor;
        this.emissive = emissive;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return ChrysalisParticles.COLORED_DUST_PLUME;
    }

    public Vector3f getColor() {
        return ARGB.vector3fFromRGB24(this.color);
    }

    public boolean shouldRandomizeColor() {
        return this.randomizeColor;
    }

    public boolean isEmissive() {
        return this.emissive;
    }
}