package net.sydokiddo.chrysalis.registry.misc;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
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
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.client.particles.ColoredDustPlumeParticle;
import net.sydokiddo.chrysalis.client.particles.SparkleParticle;
import net.sydokiddo.chrysalis.client.particles.options.ColoredDustPlumeParticleOptions;
import net.sydokiddo.chrysalis.client.particles.options.SparkleParticleOptions;
import net.sydokiddo.chrysalis.misc.util.CoreRegistry;
import net.sydokiddo.chrysalis.mixin.util.SimpleParticleTypeAccessor;
import org.jetbrains.annotations.NotNull;
import java.util.function.Function;

public class ChrysalisParticles {

    public static final net.sydokiddo.chrysalis.misc.util.CoreRegistry<ParticleType<?>> PARTICLES = CoreRegistry.create(Registries.PARTICLE_TYPE, Chrysalis.MOD_ID);

    // region Particles

    public static final SimpleParticleType
        MEMORY_FLAME = registerSimpleParticle("memory_flame", false),
        RADIANCE = registerSimpleParticle("radiance", false)
    ;

    public static final ParticleType<ColoredDustPlumeParticleOptions> COLORED_DUST_PLUME = registerAdvancedParticle(
        "colored_dust_plume", false, particleType -> ColoredDustPlumeParticleOptions.CODEC, particleType -> ColoredDustPlumeParticleOptions.STREAM_CODEC
    );

    public static final ParticleType<SparkleParticleOptions> SPARKLE = registerAdvancedParticle(
        "sparkle", false, particleType -> SparkleParticleOptions.CODEC, particleType -> SparkleParticleOptions.STREAM_CODEC
    );

    // endregion

    // region Registry

    @SuppressWarnings("all")
    private static SimpleParticleType registerSimpleParticle(String name, boolean alwaysSpawn) {
        return PARTICLES.register(name, SimpleParticleTypeAccessor.chrysalis$invokeSimpleParticleType(alwaysSpawn));
    }

    @SuppressWarnings("all")
    private static <T extends ParticleOptions> ParticleType<T> registerAdvancedParticle(String name, boolean alwaysSpawn, Function<ParticleType<T>, MapCodec<T>> firstFunction, Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> secondFunction) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Chrysalis.id(name), new ParticleType<T>(alwaysSpawn) {

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

    @Environment(EnvType.CLIENT)
    public static void registerParticles() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(MEMORY_FLAME, FlameParticle.Provider::new);
        registry.register(RADIANCE, SpellParticle.Provider::new);
        registry.register(COLORED_DUST_PLUME, ColoredDustPlumeParticle.ColoredDustPlumeParticleProvider::new);
        registry.register(SPARKLE, SparkleParticle.SparkleParticleProvider::new);
    }

    // endregion
}