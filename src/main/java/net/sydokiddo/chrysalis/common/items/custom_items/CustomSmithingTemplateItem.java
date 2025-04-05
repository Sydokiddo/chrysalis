package net.sydokiddo.chrysalis.common.items.custom_items;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import net.sydokiddo.chrysalis.Chrysalis;
import java.util.List;

public class CustomSmithingTemplateItem extends SmithingTemplateItem {

    public CustomSmithingTemplateItem(Component appliesTo, Component ingredients, Component baseSlotDescription, Component additionsSlotDescription, List<ResourceLocation> baseSlotEmptyIcons, List<ResourceLocation> additionalSlotEmptyIcons, Item.Properties properties) {
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
            List.of(
                HELMET,
                SWORD,
                CHESTPLATE,
                PICKAXE,
                LEGGINGS,
                AXE,
                BOOTS,
                HOE,
                SHOVEL
            ),
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
        HELMET = ResourceLocation.withDefaultNamespace("container/slot/helmet"),
        CHESTPLATE = ResourceLocation.withDefaultNamespace("container/slot/chestplate"),
        LEGGINGS = ResourceLocation.withDefaultNamespace("container/slot/leggings"),
        BOOTS = ResourceLocation.withDefaultNamespace("container/slot/boots"),
        ELYTRA = Chrysalis.resourceLocationId("slots/elytra"),
        SADDLE = ResourceLocation.withDefaultNamespace("container/slot/saddle"),
        HORSE_ARMOR = ResourceLocation.withDefaultNamespace("container/slot/horse_armor"),
        WOLF_ARMOR = Chrysalis.resourceLocationId("slots/wolf_armor"),

        SWORD = ResourceLocation.withDefaultNamespace("container/slot/sword"),
        PICKAXE = ResourceLocation.withDefaultNamespace("container/slot/pickaxe"),
        AXE = ResourceLocation.withDefaultNamespace("container/slot/axe"),
        SHOVEL = ResourceLocation.withDefaultNamespace("container/slot/shovel"),
        HOE = ResourceLocation.withDefaultNamespace("container/slot/hoe"),
        SHEARS = Chrysalis.resourceLocationId("slots/shears"),
        BOW = Chrysalis.resourceLocationId("slots/bow"),
        CROSSBOW = Chrysalis.resourceLocationId("slots/crossbow"),
        SHIELD = ResourceLocation.withDefaultNamespace("container/slot/shield"),
        FISHING_ROD = Chrysalis.resourceLocationId("slots/fishing_rod"),
        FLINT_AND_STEEL = Chrysalis.resourceLocationId("slots/flint_and_steel"),
        BRUSH = Chrysalis.resourceLocationId("slots/brush"),
        COMPASS = Chrysalis.resourceLocationId("slots/compass"),
        SPYGLASS = Chrysalis.resourceLocationId("slots/spyglass"),
        BUNDLE = Chrysalis.resourceLocationId("slots/bundle"),

        INGOT = ResourceLocation.withDefaultNamespace("container/slot/ingot"),
        REDSTONE_DUST = ResourceLocation.withDefaultNamespace("container/slot/redstone_dust"),
        EMERALD = ResourceLocation.withDefaultNamespace("container/slot/emerald"),
        PRISMARINE_CRYSTALS = Chrysalis.resourceLocationId("slots/prismarine_crystals"),
        DIAMOND = ResourceLocation.withDefaultNamespace("container/slot/diamond"),
        LAPIS_LAZULI = ResourceLocation.withDefaultNamespace("container/slot/lapis_lazuli"),
        ECHO_SHARD = Chrysalis.resourceLocationId("slots/echo_shard"),
        AMETHYST_SHARD = ResourceLocation.withDefaultNamespace("container/slot/amethyst_shard"),
        QUARTZ = ResourceLocation.withDefaultNamespace("container/slot/quartz"),
        NETHERITE_SCRAP = Chrysalis.resourceLocationId("slots/netherite_scrap")
    ;
}