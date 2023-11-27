package net.sydokiddo.chrysalis.misc.util.entities;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.CoreRegistry;
import net.sydokiddo.chrysalis.mixin.util.MemoryModuleAccessor;
import java.util.Optional;

@SuppressWarnings("all")
public class ChrysalisMemoryModules {

    public static final net.sydokiddo.chrysalis.misc.util.CoreRegistry<MemoryModuleType<?>> MEMORY_MODULES = CoreRegistry.create(Registries.MEMORY_MODULE_TYPE, Chrysalis.MOD_ID);

    /**
     * Template memory modules that can be used for mobs.
     **/

    public static final MemoryModuleType<LivingEntity> NEAREST_VISIBLE_AVOIDABLE_ENTITY = registerMemoryModuleWithoutCodec("nearest_visible_avoidable_entity");

    // Registry

    private static <U> MemoryModuleType<U> registerMemoryModuleWithCodec(String id, Codec<U> codec) {
        return MEMORY_MODULES.register(id, net.sydokiddo.chrysalis.mixin.util.MemoryModuleAccessor.createMemoryModuleType(Optional.of(codec)));
    }

    private static <U> MemoryModuleType<U> registerMemoryModuleWithoutCodec(String id) {
        return MEMORY_MODULES.register(id, MemoryModuleAccessor.createMemoryModuleType(Optional.empty()));
    }
}