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
public class RisingDustParticleOptions extends ScalableParticleOptionsBase implements ParticleCommonMethods {

    public static final MapCodec<RisingDustParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", Color.LIGHT_GRAY.getRGB()).forGetter(ParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf("randomize_color", false).forGetter(ParticleCommonMethods::shouldRandomizeColor),
        Codec.FLOAT.optionalFieldOf("starting_brightness", 0.0F).forGetter(RisingDustParticleOptions::getStartingBrightness),
        Codec.FLOAT.optionalFieldOf("ending_brightness", 0.0F).forGetter(RisingDustParticleOptions::getEndingBrightness),
        SCALE.optionalFieldOf("scale", 1.0F).forGetter(ScalableParticleOptionsBase::getScale))
    .apply(instance, RisingDustParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RisingDustParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.FLOAT, RisingDustParticleOptions::getStartingBrightness,
        ByteBufCodecs.FLOAT, RisingDustParticleOptions::getEndingBrightness,
        ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale,
        RisingDustParticleOptions::new
    );

    private final int color;
    private final boolean randomizeColor;
    private final float startingBrightness;
    private final float endingBrightness;

    public RisingDustParticleOptions(int color, boolean randomizeColor, float startingBrightness, float endingBrightness, float scale) {
        super(scale);
        this.color = color;
        this.randomizeColor = randomizeColor;
        this.startingBrightness = startingBrightness;
        this.endingBrightness = endingBrightness;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public boolean shouldRandomizeColor() {
        return this.randomizeColor;
    }

    public float getStartingBrightness() {
        return this.startingBrightness;
    }

    public float getEndingBrightness() {
        return this.endingBrightness;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return ChrysalisParticles.RISING_DUST;
    }
}