package net.sydokiddo.chrysalis.registry.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.sydokiddo.chrysalis.Chrysalis;

public class ChrysalisAttributes {

    // region Attributes

    public static final Holder<Attribute>
        REDUCED_DETECTION_RANGE = registerAttribute("reduced_detection_range", 0.0D, 0.0D, 1.0D),
        DAMAGE_CAPACITY = registerAttribute("damage_capacity", Double.MAX_VALUE, 1.0D, Double.MAX_VALUE),
        ENCOUNTER_MUSIC_RANGE = registerAttribute("encounter_music_range", 64.0D, 0.0D, 128.0D)
    ;

    // endregion

    // region Registry

    public static Holder<Attribute> registerAttribute(String name, double baseValue, double minValue, double maxValue) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Chrysalis.id(name), new RangedAttribute("attribute.chrysalis." + name, baseValue, minValue, maxValue).setSyncable(true));
    }

    public static void registerAttributes() {}

    // endregion
}