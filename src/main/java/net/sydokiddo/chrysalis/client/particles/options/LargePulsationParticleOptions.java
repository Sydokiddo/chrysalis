package net.sydokiddo.chrysalis.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

public class LargePulsationParticleOptions extends SmallPulsationParticleOptions {

    /**
     * The options class for large pulsation particles.
     **/

    public static final MapCodec<LargePulsationParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.colorString, Color.LIGHT_GRAY.getRGB()).forGetter(ParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.randomizeColorString, false).forGetter(ParticleCommonMethods::shouldRandomizeColor),
        Codec.INT.optionalFieldOf(ParticleCommonMethods.directionString, 1).forGetter(SmallPulsationParticleOptions::getDirection),
        LIFE_TIME.optionalFieldOf(ParticleCommonMethods.lifeTimeString, 10).forGetter(SmallPulsationParticleOptions::getLifeTime))
    .apply(instance, LargePulsationParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LargePulsationParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.INT, SmallPulsationParticleOptions::getDirection,
        ByteBufCodecs.INT, SmallPulsationParticleOptions::getLifeTime,
        LargePulsationParticleOptions::new
    );

    public LargePulsationParticleOptions(int color, boolean randomizeColor, int direction, int lifeTime) {
        super(color, randomizeColor, direction, lifeTime);
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return ChrysalisParticles.LARGE_PULSATION;
    }
}