package net.sydokiddo.chrysalis.registry.misc;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.client.particles.types.*;
import net.sydokiddo.chrysalis.client.particles.options.*;
import org.jetbrains.annotations.NotNull;
import java.util.function.Supplier;

public class ChrysalisParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, ChrysalisMod.MOD_ID);

    // region Simple Particles

    public static final Supplier<SimpleParticleType>
        MEMORY_FLAME = PARTICLE_TYPES.register("memory_flame", () -> new SimpleParticleType(false)),
        RADIANCE = PARTICLE_TYPES.register("radiance", () -> new SimpleParticleType(false)),
        ARTHROPOD_SIGHT = PARTICLE_TYPES.register("arthropod_sight", () -> new SimpleParticleType(false)),
        CREEPER_SIGHT = PARTICLE_TYPES.register("creeper_sight", () -> new SimpleParticleType(false)),
        ENDER_SIGHT = PARTICLE_TYPES.register("ender_sight", () -> new SimpleParticleType(false))
    ;

    // endregion

    // region Advanced Particles

    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredDustPlumeParticleOptions>> COLORED_DUST_PLUME = PARTICLE_TYPES.register("colored_dust_plume", () -> new ParticleType<>(false) {

        @Override
        public @NotNull MapCodec<ColoredDustPlumeParticleOptions> codec() {
            return ColoredDustPlumeParticleOptions.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ColoredDustPlumeParticleOptions> streamCodec() {
            return ColoredDustPlumeParticleOptions.STREAM_CODEC;
        }
    });

    public static final DeferredHolder<ParticleType<?>, ParticleType<DustExplosionParticleOptions>> DUST_EXPLOSION = PARTICLE_TYPES.register("dust_explosion", () -> new ParticleType<>(false) {

        @Override
        public @NotNull MapCodec<DustExplosionParticleOptions> codec() {
            return DustExplosionParticleOptions.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, DustExplosionParticleOptions> streamCodec() {
            return DustExplosionParticleOptions.STREAM_CODEC;
        }
    });

    public static final DeferredHolder<ParticleType<?>, ParticleType<RotatingDustParticleOptions>> ROTATING_DUST = PARTICLE_TYPES.register("rotating_dust", () -> new ParticleType<>(false) {

        @Override
        public @NotNull MapCodec<RotatingDustParticleOptions> codec() {
            return RotatingDustParticleOptions.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, RotatingDustParticleOptions> streamCodec() {
            return RotatingDustParticleOptions.STREAM_CODEC;
        }
    });

    public static final DeferredHolder<ParticleType<?>, ParticleType<SparkleParticleOptions>> SPARKLE = PARTICLE_TYPES.register("sparkle", () -> new ParticleType<>(false) {

        @Override
        public @NotNull MapCodec<SparkleParticleOptions> codec() {
            return SparkleParticleOptions.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, SparkleParticleOptions> streamCodec() {
            return SparkleParticleOptions.STREAM_CODEC;
        }
    });

    public static final DeferredHolder<ParticleType<?>, ParticleType<SmallPulsationParticleOptions>> SMALL_PULSATION = PARTICLE_TYPES.register("small_pulsation", () -> new ParticleType<>(false) {

        @Override
        public @NotNull MapCodec<SmallPulsationParticleOptions> codec() {
            return SmallPulsationParticleOptions.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, SmallPulsationParticleOptions> streamCodec() {
            return SmallPulsationParticleOptions.STREAM_CODEC;
        }
    });

    public static final DeferredHolder<ParticleType<?>, ParticleType<LargePulsationParticleOptions>> LARGE_PULSATION = PARTICLE_TYPES.register("large_pulsation", () -> new ParticleType<>(false) {

        @Override
        public @NotNull MapCodec<LargePulsationParticleOptions> codec() {
            return LargePulsationParticleOptions.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, LargePulsationParticleOptions> streamCodec() {
            return LargePulsationParticleOptions.STREAM_CODEC;
        }
    });

    // endregion

    // region Registry

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerClient(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MEMORY_FLAME.get(), FlameParticle.Provider::new);
        event.registerSpriteSet(RADIANCE.get(), SpellParticle.Provider::new);
        event.registerSpriteSet(ARTHROPOD_SIGHT.get(), SpellParticle.Provider::new);
        event.registerSpriteSet(CREEPER_SIGHT.get(), SpellParticle.Provider::new);
        event.registerSpriteSet(ENDER_SIGHT.get(), SpellParticle.Provider::new);
        event.registerSpriteSet(COLORED_DUST_PLUME.get(), ColoredDustPlumeParticle.Provider::new);
        event.registerSpriteSet(DUST_EXPLOSION.get(), DustExplosionParticle.Provider::new);
        event.registerSpriteSet(ROTATING_DUST.get(), RotatingDustParticle.Provider::new);
        event.registerSpriteSet(SPARKLE.get(), SparkleParticle.Provider::new);
        event.registerSpriteSet(SMALL_PULSATION.get(), PulsationParticle.SmallProvider::new);
        event.registerSpriteSet(LARGE_PULSATION.get(), PulsationParticle.LargeProvider::new);
    }

    public static void registerServer(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    // endregion
}