package net.sydokiddo.chrysalis.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.common.misc.CParticles;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class SparkParticleOptions extends ScalableParticleOptionsBase implements ParticleCommonMethods {

    /**
     * The options class for spark particles.
     **/

    public static final MapCodec<SparkParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.colorString, Color.LIGHT_GRAY.getRGB()).forGetter(ParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.randomizeColorString, false).forGetter(ParticleCommonMethods::shouldRandomizeColor),
        SCALE.optionalFieldOf(ParticleCommonMethods.scaleString, 1.0F).forGetter(ScalableParticleOptionsBase::getScale))
    .apply(instance, SparkParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SparkParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale,
        SparkParticleOptions::new
    );

    private final int color;
    private final boolean randomizeColor;

    public SparkParticleOptions(int color, boolean randomizeColor, float scale) {
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
        return CParticles.SPARK.get();
    }
}