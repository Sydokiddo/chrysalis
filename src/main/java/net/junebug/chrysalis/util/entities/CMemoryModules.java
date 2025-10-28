package net.junebug.chrysalis.util.entities;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.junebug.chrysalis.Chrysalis;
import java.util.Optional;

@SuppressWarnings("unused")
public class CMemoryModules {

    /**
     * Template memory modules that can be used for mobs.
     **/

    private static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, Chrysalis.MOD_ID);

    // region Memory Modules

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<LivingEntity>> NEAREST_VISIBLE_AVOIDABLE_ENTITY = MEMORY_MODULES.register("nearest_visible_avoidable_entity", builder -> new MemoryModuleType<>(Optional.empty()));

    // endregion

    // region Registry

    public static void register(IEventBus eventBus) {
        MEMORY_MODULES.register(eventBus);
    }

    // endregion
}