package net.sydokiddo.chrysalis.common.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sydokiddo.chrysalis.Chrysalis;

public class CAttributes {

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, Chrysalis.MOD_ID);

    // region Attributes

    public static final DeferredHolder<Attribute, Attribute>
        REDUCED_DETECTION_RANGE = registerAttribute("reduced_detection_range", 0.0D, 0.0D, 1.0D),
        DAMAGE_CAPACITY = registerAttribute("damage_capacity", Double.MAX_VALUE, 1.0D, Double.MAX_VALUE),
        ITEM_PICK_UP_RANGE = registerAttribute("item_pick_up_range", 1.0D, 1.0D, 64.0D),
        EXPERIENCE_PICK_UP_RANGE = registerAttribute("experience_pick_up_range", 1.0D, 1.0D, 64.0D)
    ;

    // endregion

    // region Registry

    private static DeferredHolder<Attribute, Attribute> registerAttribute(String name, double baseValue, double minValue, double maxValue) {
        return ATTRIBUTES.register(name, builder -> new RangedAttribute("attribute." + Chrysalis.MOD_ID + "." + name, baseValue, minValue, maxValue).setSyncable(true));
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    // endregion
}