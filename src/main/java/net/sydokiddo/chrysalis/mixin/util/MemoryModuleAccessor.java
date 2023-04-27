package net.sydokiddo.chrysalis.mixin.util;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.Optional;

@SuppressWarnings("ALL")
@Mixin(MemoryModuleType.class)
public interface MemoryModuleAccessor {

    @Invoker("<init>")
    static <U> MemoryModuleType<U> createMemoryModuleType(Optional<Codec<U>> optional) {
        throw new UnsupportedOperationException();
    }
}