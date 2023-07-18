package net.sydokiddo.chrysalis.mixin.util;

import net.minecraft.core.particles.SimpleParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("ALL")
@Mixin(SimpleParticleType.class)
public interface SimpleParticleTypeAccessor {

    /**
     * Accesses the simple particle type class.
     **/

    @Invoker("<init>")
    static SimpleParticleType createSimpleParticleType(boolean bl) {
        throw new UnsupportedOperationException();
    }
}