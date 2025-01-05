package net.sydokiddo.chrysalis.misc.util.helpers;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import java.util.List;

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
            itemContainerContents.nonEmptyStream().anyMatch(matchingItems -> matchingItems.is(item))) {
                return true;
            }
        }
        return false;
    }

    // endregion

    // region Enchantments and Armor Trims

    /**
     * Checks for enchantments or armor trims.
     **/

    public static int getEnchantmentLevel(ItemStack itemStack, ResourceKey<Enchantment> enchantment) {
        int noEnchantments = 0;
        ItemEnchantments itemEnchantments = itemStack.get(DataComponents.ENCHANTMENTS);
        if (itemEnchantments == null || itemEnchantments.isEmpty()) return noEnchantments;

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            if (entry.getKey().is(enchantment)) return EnchantmentHelper.getItemEnchantmentLevel(entry.getKey(), itemStack);
            return noEnchantments;
        }
        return noEnchantments;
    }

    public static boolean hasArmorTrim(ItemStack itemStack) {
        return itemStack.has(DataComponents.TRIM);
    }

    public static boolean hasEnchantmentOrTrim(ItemStack itemStack) {
        return itemStack.isEnchanted() || hasArmorTrim(itemStack);
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

    // region Custom Item Tooltips

    /**
     * Custom tooltips for items.
     **/

    public static final Component
        NONE_COMPONENT = Component.translatable("gui.chrysalis.none"),
        UNKNOWN_COMPONENT = Component.translatable("gui.chrysalis.unknown")
    ;

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

    public static void addCoordinatesTooltip(List<Component> tooltip, int x, int y, int z) {
        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.coordinates", x, y, z).withStyle(ChatFormatting.BLUE)));
    }

    public static void addDirectionTooltip(List<Component> tooltip, Minecraft minecraft) {
        if (minecraft.player != null) {
            Component direction = Component.translatable("gui.chrysalis.direction." + minecraft.player.getDirection().getName()).withStyle(ChatFormatting.BLUE);
            tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.facing_direction", direction).withStyle(ChatFormatting.BLUE)));
        }
    }

    public static void addDimensionTooltip(List<Component> tooltip, String dimension) {
        String registryKey = dimension.split(":")[0];
        String registryPath = dimension.split(":")[1];
        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.dimension", Component.translatable("dimension." + registryKey + "." + registryPath).withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.BLUE)));
    }

    public static Component getWeatherComponent(Level level, Holder<Biome> biome, BlockPos blockPos) {

        MutableComponent weatherType;

        if (level.isRainingAt(blockPos)) {
            if (level.isThundering()) weatherType = Component.translatable("gui.chrysalis.weather.thundering");
            else weatherType = Component.translatable("gui.chrysalis.weather.raining");
        } else {
            if (level.isRaining() && biome.value().getPrecipitationAt(blockPos, level.getSeaLevel()) == Biome.Precipitation.SNOW) weatherType = Component.translatable("gui.chrysalis.weather.snowing");
            else weatherType = Component.translatable("gui.chrysalis.weather.clear");
        }

        return weatherType;
    }

    public static Component getMoonPhaseComponent(Level level) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && !minecraft.level.dimensionType().hasFixedTime()) return Component.translatable("gui.chrysalis.moon_phase." + (level.getMoonPhase() + 1));
        else return Component.translatable("gui.chrysalis.none");
    }

    public static void addSpaceOnTooltipIfEnchantedOrTrimmed(ItemStack itemStack, List<Component> tooltip) {
        if (hasEnchantmentOrTrim(itemStack)) tooltip.add(CommonComponents.EMPTY);
    }

    public static void addNullTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.space().append(NONE_COMPONENT.copy().withStyle(ChatFormatting.BLUE)));
    }

    public static void addUnknownTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.space().append(UNKNOWN_COMPONENT.copy().withStyle(ChatFormatting.BLUE)));
    }

    public static Component addTooltipWithIcon(Component icon, Component tooltip) {
        return Component.translatable("gui.chrysalis.item.tooltip_with_icon", icon, tooltip);
    }

    public static void setTooltipIconsFont(MutableComponent mutableComponent, String modID) {
        mutableComponent.setStyle(mutableComponent.getStyle().withFont(ResourceLocation.fromNamespaceAndPath(modID, ChrysalisRegistry.TOOLTIP_ICONS_NAME)));
    }

    // endregion
}