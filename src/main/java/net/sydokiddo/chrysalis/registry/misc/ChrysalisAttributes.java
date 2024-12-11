package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.sydokiddo.chrysalis.Chrysalis;

public class ChrysalisAttributes {

    public static final Holder<Attribute>
        GENERIC_REDUCED_DETECTION_RANGE = registerAttribute("generic.reduced_detection_range", 0.0D, 0.0D, 1.0D),
        GENERIC_DAMAGE_CAPACITY = registerAttribute("generic.damage_capacity", Double.MAX_VALUE, 1.0D, Double.MAX_VALUE)
    ;

    public static Holder<Attribute> registerAttribute(String name, double baseValue, double minValue, double maxValue) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Chrysalis.id(name), new RangedAttribute("attribute.chrysalis." + name, baseValue, minValue, maxValue).setSyncable(true));
    }

    public static void registerAttributes() {}
}