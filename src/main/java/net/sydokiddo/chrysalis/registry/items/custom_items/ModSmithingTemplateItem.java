package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import java.util.List;

@SuppressWarnings("unused")
public class ModSmithingTemplateItem extends SmithingTemplateItem {

    public ModSmithingTemplateItem(Component appliesTo, Component ingredients, Component baseSlotDescription, Component additionsSlotDescription, List<ResourceLocation> baseSlotEmptyIcons, List<ResourceLocation> additionalSlotEmptyIcons, Item.Properties properties) {
        super(appliesTo, ingredients, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additionalSlotEmptyIcons, properties);
    }

    /**
     * Class for assisting with the creation of custom Smithing Template items.
     **/

    public static final ChatFormatting
        TITLE_FORMAT = ChatFormatting.GRAY,
        DESCRIPTION_FORMAT = ChatFormatting.BLUE
    ;

    public static final ResourceLocation
        EMPTY_SLOT_HELMET = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_helmet"),
        EMPTY_SLOT_CHESTPLATE = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
        EMPTY_SLOT_LEGGINGS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
        EMPTY_SLOT_BOOTS = ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots"),

        EMPTY_SLOT_SWORD = ResourceLocation.withDefaultNamespace("item/empty_slot_sword"),
        EMPTY_SLOT_PICKAXE = ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe"),
        EMPTY_SLOT_AXE = ResourceLocation.withDefaultNamespace("item/empty_slot_axe"),
        EMPTY_SLOT_SHOVEL = ResourceLocation.withDefaultNamespace("item/empty_slot_shovel"),
        EMPTY_SLOT_HOE = ResourceLocation.withDefaultNamespace("item/empty_slot_hoe"),

        EMPTY_SLOT_INGOT = ResourceLocation.withDefaultNamespace("item/empty_slot_ingot"),
        EMPTY_SLOT_REDSTONE_DUST = ResourceLocation.withDefaultNamespace("item/empty_slot_redstone_dust"),
        EMPTY_SLOT_EMERALD = ResourceLocation.withDefaultNamespace("item/empty_slot_emerald"),
        EMPTY_SLOT_DIAMOND = ResourceLocation.withDefaultNamespace("item/empty_slot_diamond"),
        EMPTY_SLOT_LAPIS_LAZULI = ResourceLocation.withDefaultNamespace("item/empty_slot_lapis_lazuli"),
        EMPTY_SLOT_AMETHYST_SHARD = ResourceLocation.withDefaultNamespace("item/empty_slot_amethyst_shard"),
        EMPTY_SLOT_QUARTZ = ResourceLocation.withDefaultNamespace("item/empty_slot_quartz")
    ;
}