package net.sydokiddo.chrysalis.registry.misc;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.client.particles.types.*;
import net.sydokiddo.chrysalis.client.particles.options.*;
import net.sydokiddo.chrysalis.util.ChrysalisCoreRegistry;
import net.sydokiddo.chrysalis.mixin.util.SimpleParticleTypeAccessor;
import org.jetbrains.annotations.NotNull;
import java.util.function.Function;

public class ChrysalisParticles {

    public static final ChrysalisCoreRegistry<ParticleType<?>> PARTICLES = ChrysalisCoreRegistry.create(Registries.PARTICLE_TYPE, ChrysalisMod.MOD_ID);

    // region Particles

    public static final SimpleParticleType
        MEMORY_FLAME = registerSimpleParticle("memory_flame", false),
        RADIANCE = registerSimpleParticle("radiance", false),
        ARTHROPOD_SIGHT = registerSimpleParticle("arthropod_sight", false),
        CREEPER_SIGHT = registerSimpleParticle("creeper_sight", false),
        ENDER_SIGHT = registerSimpleParticle("ender_sight", false)
    ;

    public static final ParticleType<ColoredDustPlumeParticleOptions> COLORED_DUST_PLUME = registerAdvancedParticle(
        "colored_dust_plume", false, particleType -> ColoredDustPlumeParticleOptions.CODEC, particleType -> ColoredDustPlumeParticleOptions.STREAM_CODEC
    );

    public static final ParticleType<DustExplosionParticleOptions> DUST_EXPLOSION = registerAdvancedParticle(
        "dust_explosion", false, particleType -> DustExplosionParticleOptions.CODEC, particleType -> DustExplosionParticleOptions.STREAM_CODEC
    );

    public static final ParticleType<RotatingDustParticleOptions> ROTATING_DUST = registerAdvancedParticle(
        "rotating_dust", false, particleType -> RotatingDustParticleOptions.CODEC, particleType -> RotatingDustParticleOptions.STREAM_CODEC
    );

    public static final ParticleType<SparkleParticleOptions> SPARKLE = registerAdvancedParticle(
        "sparkle", false, particleType -> SparkleParticleOptions.CODEC, particleType -> SparkleParticleOptions.STREAM_CODEC
    );

    public static final ParticleType<SmallPulsationParticleOptions> SMALL_PULSATION = registerAdvancedParticle(
        "small_pulsation", true, particleType -> SmallPulsationParticleOptions.CODEC, particleType -> SmallPulsationParticleOptions.STREAM_CODEC
    );

    public static final ParticleType<LargePulsationParticleOptions> LARGE_PULSATION = registerAdvancedParticle(
        "large_pulsation", true, particleType -> LargePulsationParticleOptions.CODEC, particleType -> LargePulsationParticleOptions.STREAM_CODEC
    );

    // endregion

    // region Registry

    @SuppressWarnings("all")
    private static SimpleParticleType registerSimpleParticle(String name, boolean alwaysSpawn) {
        return PARTICLES.register(name, SimpleParticleTypeAccessor.chrysalis$invokeSimpleParticleType(alwaysSpawn));
    }

    private static <T extends ParticleOptions> ParticleType<T> registerAdvancedParticle(String name, boolean alwaysSpawn, Function<ParticleType<T>, MapCodec<T>> firstFunction, Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> secondFunction) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, ChrysalisMod.id(name), new ParticleType<T>(alwaysSpawn) {

            @Override
            public @NotNull MapCodec<T> codec() {
                return firstFunction.apply(this);
            }

            @Override
            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return secondFunction.apply(this);
            }
        });
    }

    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MEMORY_FLAME, FlameParticle.Provider::new);
        event.registerSpriteSet(RADIANCE, SpellParticle.Provider::new);
        event.registerSpriteSet(ARTHROPOD_SIGHT, SpellParticle.Provider::new);
        event.registerSpriteSet(CREEPER_SIGHT, SpellParticle.Provider::new);
        event.registerSpriteSet(ENDER_SIGHT, SpellParticle.Provider::new);
        event.registerSpriteSet(COLORED_DUST_PLUME, ColoredDustPlumeParticle.ColoredDustPlumeParticleProvider::new);
        event.registerSpriteSet(DUST_EXPLOSION, DustExplosionParticle.DustExplosionParticleProvider::new);
        event.registerSpriteSet(ROTATING_DUST, RotatingDustParticle.RotatingDustParticleProvider::new);
        event.registerSpriteSet(SPARKLE, SparkleParticle.SparkleParticleProvider::new);
        event.registerSpriteSet(SMALL_PULSATION, PulsationParticle.SmallPulsationParticleProvider::new);
        event.registerSpriteSet(LARGE_PULSATION, PulsationParticle.LargePulsationParticleProvider::new);
    }

    // endregion
}