package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import java.util.List;

@SuppressWarnings("unused")
public class ModSmithingTemplateItem extends SmithingTemplateItem {

    public ModSmithingTemplateItem(Component appliesTo, Component ingredients, Component upgradeDescription, Component baseSlotDescription, Component additionsSlotDescription, List<ResourceLocation> baseSlotEmptyIcons, List<ResourceLocation> additionalSlotEmptyIcons) {
        super(appliesTo, ingredients, upgradeDescription, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additionalSlotEmptyIcons);
    }

    /**
     * Class for assisting with the creation of custom Smithing Template items.
     **/

    public static final ChatFormatting
        TITLE_FORMAT = ChatFormatting.GRAY,
        DESCRIPTION_FORMAT = ChatFormatting.BLUE
    ;

    public static final ResourceLocation
        EMPTY_SLOT_HELMET = ResourceLocation.parse("item/empty_armor_slot_helmet"),
        EMPTY_SLOT_CHESTPLATE = ResourceLocation.parse("item/empty_armor_slot_chestplate"),
        EMPTY_SLOT_LEGGINGS = ResourceLocation.parse("item/empty_armor_slot_leggings"),
        EMPTY_SLOT_BOOTS = ResourceLocation.parse("item/empty_armor_slot_boots"),

        EMPTY_SLOT_SWORD = ResourceLocation.parse("item/empty_slot_sword"),
        EMPTY_SLOT_PICKAXE = ResourceLocation.parse("item/empty_slot_pickaxe"),
        EMPTY_SLOT_AXE = ResourceLocation.parse("item/empty_slot_axe"),
        EMPTY_SLOT_SHOVEL = ResourceLocation.parse("item/empty_slot_shovel"),
        EMPTY_SLOT_HOE = ResourceLocation.parse("item/empty_slot_hoe"),

        EMPTY_SLOT_INGOT = ResourceLocation.parse("item/empty_slot_ingot")
    ;
}