package net.sydokiddo.chrysalis.common.misc;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.client.particles.options.*;
import org.jetbrains.annotations.NotNull;

public class CParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Chrysalis.MOD_ID);

    // region Simple Particles

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType>
        MEMORY_FLAME = PARTICLE_TYPES.register("memory_flame", () -> createSimpleParticle(false)),
        RADIANCE = PARTICLE_TYPES.register("radiance", () -> createSimpleParticle(false)),
        ARTHROPOD_SIGHT = PARTICLE_TYPES.register("arthropod_sight", () -> createSimpleParticle(false)),
        CREEPER_SIGHT = PARTICLE_TYPES.register("creeper_sight", () -> createSimpleParticle(false)),
        ENDER_SIGHT = PARTICLE_TYPES.register("ender_sight", () -> createSimpleParticle(false))
    ;

    // endregion

    // region Advanced Particles

    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredDustPlumeParticleOptions>> COLORED_DUST_PLUME = PARTICLE_TYPES.register("colored_dust_plume", () -> createAdvancedParticle(ColoredDustPlumeParticleOptions.CODEC, ColoredDustPlumeParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredDirectionalDustParticleOptions>> COLORED_DIRECTIONAL_DUST = PARTICLE_TYPES.register("colored_directional_dust", () -> createAdvancedParticle(ColoredDirectionalDustParticleOptions.CODEC, ColoredDirectionalDustParticleOptions.STREAM_CODEC, true));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredPortalParticleOptions>> COLORED_PORTAL = PARTICLE_TYPES.register("colored_portal", () -> createAdvancedParticle(ColoredPortalParticleOptions.CODEC, ColoredPortalParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<DustExplosionParticleOptions>> DUST_EXPLOSION = PARTICLE_TYPES.register("dust_explosion", () -> createAdvancedParticle(DustExplosionParticleOptions.CODEC, DustExplosionParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<RotatingDustParticleOptions>> ROTATING_DUST = PARTICLE_TYPES.register("rotating_dust", () -> createAdvancedParticle(RotatingDustParticleOptions.CODEC, RotatingDustParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<SparkleParticleOptions>> SPARKLE = PARTICLE_TYPES.register("sparkle", () -> createAdvancedParticle(SparkleParticleOptions.CODEC, SparkleParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<SparkParticleOptions>> SPARK = PARTICLE_TYPES.register("spark", () -> createAdvancedParticle(SparkParticleOptions.CODEC, SparkParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<SmallPulsationParticleOptions>> SMALL_PULSATION = PARTICLE_TYPES.register("small_pulsation", () -> createAdvancedParticle(SmallPulsationParticleOptions.CODEC, SmallPulsationParticleOptions.STREAM_CODEC, true));
    public static final DeferredHolder<ParticleType<?>, ParticleType<LargePulsationParticleOptions>> LARGE_PULSATION = PARTICLE_TYPES.register("large_pulsation", () -> createAdvancedParticle(LargePulsationParticleOptions.CODEC, LargePulsationParticleOptions.STREAM_CODEC, true));

    // endregion

    // region Registry

    public static SimpleParticleType createSimpleParticle(boolean alwaysShow) {
        return new SimpleParticleType(alwaysShow);
    }

    public static <T extends ParticleOptions> ParticleType<T> createAdvancedParticle(MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean alwaysShow) {
        return new ParticleType<>(alwaysShow) {

            @Override
            public @NotNull MapCodec<T> codec() {
                return codec;
            }

            @Override
            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec;
            }
        };
    }

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    // endregion
}