package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.base_classes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.items.custom_items.CustomCrosshairItem;

public class ExtraReachDebugUtilityItem extends DebugUtilityItem implements CustomCrosshairItem {

    public ExtraReachDebugUtilityItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds an entity_interaction_range attribute modifier to the item.
     **/

    public static ItemAttributeModifiers createAttributes(EquipmentSlotGroup equipmentSlotGroup) {
        return ItemAttributeModifiers.builder().add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(Chrysalis.id("base_entity_interaction_range"), 64.0D, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup).build();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean shouldDisplayCrosshair(Player player) {
        return Minecraft.getInstance().crosshairPickEntity instanceof LivingEntity;
    }
}