package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.base_classes;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.sydokiddo.chrysalis.Chrysalis;

public class ExtraReachDebugUtilityItem extends DebugUtilityItem {

    public ExtraReachDebugUtilityItem(Properties properties) {
        super(properties);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(Chrysalis.id("base_entity_interaction_range"), 64.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }
}