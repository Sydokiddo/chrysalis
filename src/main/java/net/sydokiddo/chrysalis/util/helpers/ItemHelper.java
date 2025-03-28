package net.sydokiddo.chrysalis.util.helpers;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.sydokiddo.chrysalis.common.ChrysalisRegistry;
import net.sydokiddo.chrysalis.common.items.ChrysalisDataComponents;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ItemHelper {

    // region Inventories

    /**
     * Checks to see if a block entity as an item is empty when in the player's inventory.
     **/

    public static boolean containerIsEmpty(ItemStack itemStack) {
        ItemContainerContents itemContainerContents = itemStack.get(DataComponents.CONTAINER);
        if (itemContainerContents == null) return true;
        return itemContainerContents.nonEmptyStream().toList().isEmpty();
    }

    /**
     * Checks for a specific item in the player's inventory.
     **/

    public static boolean hasItemInInventory(Item item, Player player) {

        for (int slots = 0; slots <= 35; slots++) {

            ItemStack itemStack = player.getInventory().getItem(slots);
            BundleContents bundleContents = itemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
            ItemContainerContents itemContainerContents = itemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);

            if (itemStack.is(item) || player.getOffhandItem().is(item) || player.getItemBySlot(EquipmentSlot.HEAD).is(item) || player.getItemBySlot(EquipmentSlot.CHEST).is(item) ||
            player.getItemBySlot(EquipmentSlot.LEGS).is(item) || player.getItemBySlot(EquipmentSlot.FEET).is(item) || !bundleContents.isEmpty() && bundleContents.itemCopyStream().anyMatch(matchingItems -> matchingItems.is(item)) ||
            itemContainerContents.nonEmptyStream().anyMatch(matchingItems -> matchingItems.is(item))) return true;
        }

        return false;
    }

    // endregion

    // region Item Components

    /**
     * Checks for various properties relating to enchantments.
     **/

    public static int getEnchantmentLevel(ItemStack itemStack, ResourceKey<Enchantment> enchantment) {

        int noEnchantments = 0;
        ItemEnchantments itemEnchantments = itemStack.get(DataComponents.ENCHANTMENTS);
        if (itemEnchantments == null || itemEnchantments.isEmpty()) return noEnchantments;

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            if (entry.getKey().is(enchantment)) return EnchantmentHelper.getTagEnchantmentLevel(entry.getKey(), itemStack);
            return noEnchantments;
        }

        return noEnchantments;
    }

    /**
     * Checks for various properties relating to armor trims.
     **/

    public static boolean hasArmorTrim(ItemStack itemStack) {
        return itemStack.has(DataComponents.TRIM);
    }

    public static boolean hasSpecificArmorTrim(ItemStack itemStack, Holder<TrimPattern> pattern, Holder<TrimMaterial> material) {
        return hasArmorTrim(itemStack) && Objects.requireNonNull(itemStack.get(DataComponents.TRIM)).hasPatternAndMaterial(pattern, material);
    }

    /**
     * Checks if the item contains a specific word in its name.
     **/

    public static boolean nameContains(ItemStack itemStack, String name) {
        Component customName = itemStack.getCustomName();
        return customName != null && customName.getString().contains(name);
    }

    public static boolean listContainsName(ItemStack itemStack, List<String> list) {
        for (String name : list) if (nameContains(itemStack, name)) return true;
        return false;
    }

    /**
     * Gets an item's saved entity data component.
     **/

    public static final DataComponentType<CustomData> SAVED_ENTITY_DATA_COMPONENT = ChrysalisDataComponents.SAVED_ENTITY_DATA.get();

    public static CustomData getSavedEntityData(ItemStack itemStack) {
        return itemStack.getOrDefault(SAVED_ENTITY_DATA_COMPONENT, CustomData.EMPTY);
    }

    // endregion

    // region Item Entities

    /**
     * Sets the glow color entity data of an item entity.
     **/

    public static int getItemGlowColor(ItemEntity itemEntity) {
        return itemEntity.getEntityData().get(ChrysalisRegistry.ITEM_GLOW_COLOR);
    }

    public static void setItemGlowColor(ItemEntity itemEntity, int glowColor) {
        itemEntity.getEntityData().set(ChrysalisRegistry.ITEM_GLOW_COLOR, glowColor);
    }

    // endregion

    // region Tooltips

    /**
     * Custom tooltips for items.
     **/

    public static void addHoldingTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_held").withStyle(ChatFormatting.GRAY));
    }

    public static void addUseTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_used", Minecraft.getInstance().options.keyUse.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
    }

    public static void addAttackTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_used", Minecraft.getInstance().options.keyAttack.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
    }

    public static void addFoodTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_eaten").withStyle(ChatFormatting.GRAY));
    }

    public static void addDrinkTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_drank").withStyle(ChatFormatting.GRAY));
    }

    public static void addThrowTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_thrown", Minecraft.getInstance().options.keyUse.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
    }

    public static void addCoordinatesTooltip(List<Component> tooltip, int x, int y, int z) {
        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.coordinates", x, y, z).withStyle(ChatFormatting.BLUE)));
    }

    public static void addDirectionTooltip(List<Component> tooltip, Minecraft minecraft) {
        if (minecraft.player == null) return;
        Component direction = Component.translatable("gui.chrysalis.direction." + minecraft.player.getDirection().getName()).withStyle(ChatFormatting.BLUE);
        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.facing_direction", direction).withStyle(ChatFormatting.BLUE)));
    }

    public static void addDimensionTooltip(List<Component> tooltip, String dimension) {
        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.dimension", ComponentHelper.getDimensionComponent(dimension).copy().withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.BLUE)));
    }

    public static void addNullTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.space().append(ComponentHelper.NONE.copy().withStyle(ChatFormatting.BLUE)));
    }

    public static void addUnknownTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.space().append(ComponentHelper.UNKNOWN.copy().withStyle(ChatFormatting.BLUE)));
    }

    public static Component addTooltipWithIcon(Component icon, Component tooltip) {
        return Component.translatable("gui.chrysalis.item.tooltip_with_icon", icon, tooltip);
    }

    public static Component addTooltipWithIconBothSides(Component icon, Component tooltip) {
        return Component.translatable("gui.chrysalis.item.tooltip_with_icon.both_sides", icon, tooltip, icon);
    }

    // endregion
}