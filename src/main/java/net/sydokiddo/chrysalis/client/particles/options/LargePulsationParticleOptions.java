package net.sydokiddo.chrysalis.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.sydokiddo.chrysalis.client.particles.ParticleCommonMethods;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import org.jetbrains.annotations.NotNull;
import java.awt.*;

@Environment(EnvType.CLIENT)
public class LargePulsationParticleOptions extends SmallPulsationParticleOptions {

    public static final MapCodec<LargePulsationParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf("color", Color.LIGHT_GRAY.getRGB()).forGetter(ParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf("randomize_color", false).forGetter(ParticleCommonMethods::shouldRandomizeColor),
        Codec.INT.optionalFieldOf("direction", 0).forGetter(SmallPulsationParticleOptions::getDirection))
    .apply(instance, LargePulsationParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LargePulsationParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.INT, SmallPulsationParticleOptions::getDirection,
        LargePulsationParticleOptions::new
    );

    public LargePulsationParticleOptions(int color, boolean randomizeColor, int direction) {
        super(color, randomizeColor, direction);
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return ChrysalisParticles.LARGE_PULSATION;
    }
}