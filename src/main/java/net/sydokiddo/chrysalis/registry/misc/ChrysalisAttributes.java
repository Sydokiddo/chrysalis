package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.ChrysalisMod;
import java.util.function.Supplier;

public class ChrysalisAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, ChrysalisMod.MOD_ID);

    // region Attributes

    public static final Supplier<Attribute>
        REDUCED_DETECTION_RANGE = registerAttribute("reduced_detection_range", 0.0D, 0.0D, 1.0D),
        DAMAGE_CAPACITY = registerAttribute("damage_capacity", Double.MAX_VALUE, 1.0D, Double.MAX_VALUE)
    ;

    // endregion

    // region Registry

    public static Supplier<Attribute> registerAttribute(String name, double baseValue, double minValue, double maxValue) {
        return ATTRIBUTES.register(name, builder -> new RangedAttribute("attribute.chrysalis." + name, baseValue, minValue, maxValue).setSyncable(true));
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    // endregion
}