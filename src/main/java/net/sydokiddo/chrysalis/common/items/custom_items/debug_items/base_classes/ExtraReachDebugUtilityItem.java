package net.sydokiddo.chrysalis.common.items.custom_items.debug_items.base_classes;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.ChrysalisDataComponents;
import net.sydokiddo.chrysalis.common.items.custom_items.CustomCrosshairItem;

public class ExtraReachDebugUtilityItem extends DebugUtilityItem implements CustomCrosshairItem {

    public ExtraReachDebugUtilityItem(Properties properties) {
        super(properties.component(ChrysalisDataComponents.INCREASED_PICK_RADIUS, Unit.INSTANCE));
    }

    /**
     * Adds an entity_interaction_range attribute modifier to the item.
     **/

    public static ItemAttributeModifiers createAttributes(EquipmentSlotGroup equipmentSlotGroup) {
        return ItemAttributeModifiers.builder().add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(Chrysalis.resourceLocationId("base_entity_interaction_range"), 64.0D, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup).build();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldDisplayCrosshair(Player player) {
        return Minecraft.getInstance().crosshairPickEntity instanceof LivingEntity;
    }
}