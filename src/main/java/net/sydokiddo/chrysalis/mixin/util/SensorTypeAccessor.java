package net.sydokiddo.chrysalis.mixin.util;

import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.function.Supplier;

@SuppressWarnings("ALL")
@Mixin(SensorType.class)
public interface SensorTypeAccessor {

    /**
     * Accesses the sensor type class for mobs.
     **/

    @Invoker
    static <U extends Sensor<?>> SensorType<U> callRegister(String string, Supplier<U> supplier) {
        throw new UnsupportedOperationException();
    }
}