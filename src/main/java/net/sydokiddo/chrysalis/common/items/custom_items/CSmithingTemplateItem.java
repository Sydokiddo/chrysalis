package net.sydokiddo.chrysalis.common.items.custom_items;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import java.util.List;

public class CSmithingTemplateItem extends SmithingTemplateItem {

    public CSmithingTemplateItem(Component appliesTo, Component ingredients, Component baseSlotDescription, Component additionsSlotDescription, List<ResourceLocation> baseSlotEmptyIcons, List<ResourceLocation> additionalSlotEmptyIcons, Item.Properties properties) {
        super(appliesTo, ingredients, baseSlotDescription, additionsSlotDescription, baseSlotEmptyIcons, additionalSlotEmptyIcons, properties);
    }

    /**
     * Class for assisting with the creation of custom smithing template items.
     **/

    public static SmithingTemplateItem createUpgradeTemplate(String modID, String smithingTemplateName, ResourceLocation upgradeIcon, Item.Properties properties) {
        return new SmithingTemplateItem(
            createDescription(modID, smithingTemplateName + ".applies_to", DESCRIPTION_FORMAT),
            createDescription(modID, smithingTemplateName + ".ingredients", DESCRIPTION_FORMAT),
            createDescription(modID, smithingTemplateName + ".base_slot_description", TITLE_FORMAT),
            createDescription(modID, smithingTemplateName + ".additions_slot_description", TITLE_FORMAT),
            List.of(EMPTY_SLOT_HELMET, EMPTY_SLOT_SWORD, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_PICKAXE,
            EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_AXE, EMPTY_SLOT_BOOTS, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL),
            List.of(upgradeIcon),
            properties
        );
    }

    public static final ChatFormatting
        TITLE_FORMAT = ChatFormatting.GRAY,
        DESCRIPTION_FORMAT = ChatFormatting.BLUE
    ;

    public static Component createDescription(String modID, String name, ChatFormatting chatFormatting) {
        return Component.translatable(Util.makeDescriptionId("item", ResourceLocation.fromNamespaceAndPath(modID, "smithing_template." + name))).withStyle(chatFormatting);
    }

    @SuppressWarnings("unused")
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