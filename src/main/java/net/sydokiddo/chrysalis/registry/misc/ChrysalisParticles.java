package net.sydokiddo.chrysalis.registry.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.CoreRegistry;
import net.sydokiddo.chrysalis.mixin.util.SimpleParticleTypeAccessor;

public class ChrysalisParticles {

    public static final net.sydokiddo.chrysalis.misc.util.CoreRegistry<ParticleType<?>> PARTICLES = CoreRegistry.create(Registries.PARTICLE_TYPE, Chrysalis.MOD_ID);

    public static final SimpleParticleType
        MEMORY_FLAME = register("memory_flame", false),
        RADIANCE = register("radiance", false)
    ;

    @SuppressWarnings("all")
    private static SimpleParticleType register(String name, boolean alwaysSpawn) {
        return PARTICLES.register(name, SimpleParticleTypeAccessor.chrysalis$invokeSimpleParticleType(alwaysSpawn));
    }

    @Environment(EnvType.CLIENT)
    public static void registerParticles() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(MEMORY_FLAME, FlameParticle.Provider::new);
        registry.register(RADIANCE, SpellParticle.Provider::new);
    }
}