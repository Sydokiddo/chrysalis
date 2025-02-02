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
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

@Environment(EnvType.CLIENT)
public class SparkleParticleOptions extends ScalableParticleOptionsBase implements ColoredParticleCommonMethods {

    public static final MapCodec<SparkleParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", Color.LIGHT_GRAY.getRGB()).forGetter(ColoredParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf("randomize_color", false).forGetter(ColoredParticleCommonMethods::shouldRandomizeColor),
        SCALE.optionalFieldOf("scale", 1.0F).forGetter(ScalableParticleOptionsBase::getScale))
    .apply(instance, SparkleParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SparkleParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ColoredParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ColoredParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale,
        SparkleParticleOptions::new
    );

    private final int color;
    private final boolean randomizeColor;

    public SparkleParticleOptions(int color, boolean randomizeColor, float scale) {
        super(scale);
        this.color = color;
        this.randomizeColor = randomizeColor;
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
    public @NotNull ParticleType<?> getType() {
        return ChrysalisParticles.SPARKLE;
    }
}