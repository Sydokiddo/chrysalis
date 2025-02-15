package net.sydokiddo.chrysalis.misc.util.entities;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.CoreRegistry;
import java.util.Optional;

@SuppressWarnings("all")
public class ChrysalisMemoryModules {

    /**
     * Template memory modules that can be used for mobs.
     **/

    public static final net.sydokiddo.chrysalis.misc.util.CoreRegistry<MemoryModuleType<?>> MEMORY_MODULES = CoreRegistry.create(Registries.MEMORY_MODULE_TYPE, Chrysalis.MOD_ID);

    public static final MemoryModuleType<LivingEntity> NEAREST_VISIBLE_AVOIDABLE_ENTITY = registerMemoryModuleWithoutCodec("nearest_visible_avoidable_entity");

    private static <U> MemoryModuleType<U> registerMemoryModuleWithCodec(String name, Codec<U> codec) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, Chrysalis.id(name), new MemoryModuleType<U>(Optional.of(codec)));
    }

    private static <U> MemoryModuleType<U> registerMemoryModuleWithoutCodec(String name) {
        return Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, Chrysalis.id(name), new MemoryModuleType<U>(Optional.empty()));
    }
}