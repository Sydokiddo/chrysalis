package net.sydokiddo.chrysalis.util.entities;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.util.ChrysalisCoreRegistry;
import java.util.Optional;

@SuppressWarnings("all")
public class ChrysalisMemoryModules {

    /**
     * Template memory modules that can be used for mobs.
     **/

    public static final ChrysalisCoreRegistry<MemoryModuleType<?>> MEMORY_MODULES = ChrysalisCoreRegistry.create(Registries.MEMORY_MODULE_TYPE, ChrysalisMod.MOD_ID);

    public static final MemoryModuleType<LivingEntity> NEAREST_VISIBLE_AVOIDABLE_ENTITY = registerMemoryModuleWithoutCodec("nearest_visible_avoidable_entity");

    private static <U> MemoryModuleType<U> registerMemoryModuleWithCodec(String name, Codec<U> codec) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, ChrysalisMod.id(name), new MemoryModuleType<U>(Optional.of(codec)));
    }

    private static <U> MemoryModuleType<U> registerMemoryModuleWithoutCodec(String name) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, ChrysalisMod.id(name), new MemoryModuleType<U>(Optional.empty()));
    }
}