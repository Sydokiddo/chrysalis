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
import net.minecraft.util.ExtraCodecs;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

@Environment(EnvType.CLIENT)
public class ColoredDustPlumeParticleOptions extends ScalableParticleOptionsBase implements ParticleCommonMethods {

    public static final MapCodec<ColoredDustPlumeParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", Color.LIGHT_GRAY.getRGB()).forGetter(ParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf("randomize_color", false).forGetter(ParticleCommonMethods::shouldRandomizeColor),
        Codec.BOOL.optionalFieldOf("emissive", false).forGetter(ParticleCommonMethods::isEmissive),
        SCALE.optionalFieldOf("scale", 1.0F).forGetter(ScalableParticleOptionsBase::getScale))
    .apply(instance, ColoredDustPlumeParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColoredDustPlumeParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::isEmissive,
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
        return ChrysalisParticles.COLORED_DUST_PLUME;
    }
}