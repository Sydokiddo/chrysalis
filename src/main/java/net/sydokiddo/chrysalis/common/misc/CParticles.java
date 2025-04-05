package net.sydokiddo.chrysalis.common.misc;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.client.particles.options.*;
import net.sydokiddo.chrysalis.util.helpers.RegistryHelper;

public class CParticles {

    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Chrysalis.MOD_ID);

    // region Simple Particles

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType>
        MEMORY_FLAME = PARTICLE_TYPES.register("memory_flame", () -> RegistryHelper.registerSimpleParticle(false)),
        RADIANCE = PARTICLE_TYPES.register("radiance", () -> RegistryHelper.registerSimpleParticle(false)),
        ARTHROPOD_SIGHT = PARTICLE_TYPES.register("arthropod_sight", () -> RegistryHelper.registerSimpleParticle(false)),
        BLIND_SIGHT = PARTICLE_TYPES.register("blind_sight", () -> RegistryHelper.registerSimpleParticle(false)),
        CREEPER_SIGHT = PARTICLE_TYPES.register("creeper_sight", () -> RegistryHelper.registerSimpleParticle(false)),
        ENDER_SIGHT = PARTICLE_TYPES.register("ender_sight", () -> RegistryHelper.registerSimpleParticle(false)),
        RESIN_SIGHT = PARTICLE_TYPES.register("resin_sight", () -> RegistryHelper.registerSimpleParticle(false)),
        SKELETAL_SIGHT = PARTICLE_TYPES.register("skeletal_sight", () -> RegistryHelper.registerSimpleParticle(false)),
        ZOMBIE_SIGHT = PARTICLE_TYPES.register("zombie_sight", () -> RegistryHelper.registerSimpleParticle(false))
    ;

    // endregion

    // region Advanced Particles

    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredDustPlumeParticleOptions>> COLORED_DUST_PLUME = PARTICLE_TYPES.register("colored_dust_plume", () -> RegistryHelper.registerAdvancedParticle(ColoredDustPlumeParticleOptions.CODEC, ColoredDustPlumeParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredDirectionalDustParticleOptions>> COLORED_DIRECTIONAL_DUST = PARTICLE_TYPES.register("colored_directional_dust", () -> RegistryHelper.registerAdvancedParticle(ColoredDirectionalDustParticleOptions.CODEC, ColoredDirectionalDustParticleOptions.STREAM_CODEC, true));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredPortalParticleOptions>> COLORED_PORTAL = PARTICLE_TYPES.register("colored_portal", () -> RegistryHelper.registerAdvancedParticle(ColoredPortalParticleOptions.CODEC, ColoredPortalParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<DustExplosionParticleOptions>> DUST_EXPLOSION = PARTICLE_TYPES.register("dust_explosion", () -> RegistryHelper.registerAdvancedParticle(DustExplosionParticleOptions.CODEC, DustExplosionParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<RotatingDustParticleOptions>> ROTATING_DUST = PARTICLE_TYPES.register("rotating_dust", () -> RegistryHelper.registerAdvancedParticle(RotatingDustParticleOptions.CODEC, RotatingDustParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<SparkleParticleOptions>> SPARKLE = PARTICLE_TYPES.register("sparkle", () -> RegistryHelper.registerAdvancedParticle(SparkleParticleOptions.CODEC, SparkleParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<SparkParticleOptions>> SPARK = PARTICLE_TYPES.register("spark", () -> RegistryHelper.registerAdvancedParticle(SparkParticleOptions.CODEC, SparkParticleOptions.STREAM_CODEC, false));
    public static final DeferredHolder<ParticleType<?>, ParticleType<SmallPulsationParticleOptions>> SMALL_PULSATION = PARTICLE_TYPES.register("small_pulsation", () -> RegistryHelper.registerAdvancedParticle(SmallPulsationParticleOptions.CODEC, SmallPulsationParticleOptions.STREAM_CODEC, true));
    public static final DeferredHolder<ParticleType<?>, ParticleType<LargePulsationParticleOptions>> LARGE_PULSATION = PARTICLE_TYPES.register("large_pulsation", () -> RegistryHelper.registerAdvancedParticle(LargePulsationParticleOptions.CODEC, LargePulsationParticleOptions.STREAM_CODEC, true));
    public static final DeferredHolder<ParticleType<?>, ParticleType<MusicNoteParticleOptions>> MUSIC_NOTE = PARTICLE_TYPES.register("music_note", () -> RegistryHelper.registerAdvancedParticle(MusicNoteParticleOptions.CODEC, MusicNoteParticleOptions.STREAM_CODEC, false));

    // endregion

    // region Registry

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    // endregion
}