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
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.common.misc.CParticles;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class RotatingDustParticleOptions extends ScalableParticleOptionsBase implements ParticleCommonMethods {

    /**
     * The options class for rotating dust particles.
     **/

    public static final MapCodec<RotatingDustParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.colorString, Color.LIGHT_GRAY.getRGB()).forGetter(ParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.randomizeColorString, false).forGetter(ParticleCommonMethods::shouldRandomizeColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.emissiveString, false).forGetter(ParticleCommonMethods::isEmissive),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.hasGravityString, false).forGetter(RotatingDustParticleOptions::hasGravity),
        SCALE.optionalFieldOf(ParticleCommonMethods.scaleString, 1.0F).forGetter(ScalableParticleOptionsBase::getScale))
    .apply(instance, RotatingDustParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RotatingDustParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::isEmissive,
        ByteBufCodecs.BOOL, RotatingDustParticleOptions::hasGravity,
        ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale,
        RotatingDustParticleOptions::new
    );

    private final int color;

    private final boolean
        randomizeColor,
        emissive,
        hasGravity
    ;

    public RotatingDustParticleOptions(int color, boolean randomizeColor, boolean emissive, boolean hasGravity, float scale) {
        super(scale);
        this.color = color;
        this.randomizeColor = randomizeColor;
        this.emissive = emissive;
        this.hasGravity = hasGravity;
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

    public boolean hasGravity() {
        return this.hasGravity;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return CParticles.ROTATING_DUST.get();
    }
}