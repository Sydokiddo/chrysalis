package net.sydokiddo.chrysalis.client.particles.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
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
public class SmallPulsationParticleOptions implements ParticleOptions, ParticleCommonMethods {

    /**
     * The options class for small pulsation particles.
     **/

    public static final MapCodec<SmallPulsationParticleOptions> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(ExtraCodecs.RGB_COLOR_CODEC.optionalFieldOf(ParticleCommonMethods.colorString, Color.LIGHT_GRAY.getRGB()).forGetter(ParticleCommonMethods::getColor),
        Codec.BOOL.optionalFieldOf(ParticleCommonMethods.randomizeColorString, false).forGetter(ParticleCommonMethods::shouldRandomizeColor),
        Codec.INT.optionalFieldOf(ParticleCommonMethods.directionString, 0).forGetter(SmallPulsationParticleOptions::getDirection),
        LIFE_TIME.optionalFieldOf(ParticleCommonMethods.lifeTimeString, 10).forGetter(SmallPulsationParticleOptions::getLifeTime))
    .apply(instance, SmallPulsationParticleOptions::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SmallPulsationParticleOptions> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, ParticleCommonMethods::getColor,
        ByteBufCodecs.BOOL, ParticleCommonMethods::shouldRandomizeColor,
        ByteBufCodecs.INT, SmallPulsationParticleOptions::getDirection,
        ByteBufCodecs.INT, SmallPulsationParticleOptions::getLifeTime,
        SmallPulsationParticleOptions::new
    );

    private final int color;
    private final boolean randomizeColor;
    private final int direction;
    private final int lifeTime;

    public SmallPulsationParticleOptions(int color, boolean randomizeColor, int direction, int lifeTime) {
        this.color = color;
        this.randomizeColor = randomizeColor;
        this.direction = direction;
        this.lifeTime = this.getClampedLifeTime(lifeTime);
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public boolean shouldRandomizeColor() {
        return this.randomizeColor;
    }

    public int getDirection() {
        return this.direction;
    }

    public Direction getFinalDirection() {
        return Direction.from3DDataValue(this.getDirection());
    }

    public int getLifeTime() {
        return this.lifeTime;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return ChrysalisParticles.SMALL_PULSATION;
    }
}