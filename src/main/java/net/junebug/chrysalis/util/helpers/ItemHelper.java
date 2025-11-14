package net.junebug.chrysalis.util.helpers;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.junebug.chrysalis.Chrysalis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.junebug.chrysalis.common.CRegistry;
import net.junebug.chrysalis.common.items.CDataComponents;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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

    /**
     * Checks to see if the player is holding two different items in each hand.
     **/

    public static boolean isHoldingTwoItems(Player player, Item firstItem, Item secondItem) {
        return player.getMainHandItem().is(firstItem) && player.getOffhandItem().is(secondItem) || player.getMainHandItem().is(secondItem) && player.getOffhandItem().is(firstItem);
    }

    /**
     * Allows for the player to use two items in each hand simultaneously.
     **/

    public static void useBothItems(Player player, Item firstItem, Item secondItem, boolean damageFirstItem, boolean removeFirstItem, boolean damageSecondItem, boolean removeSecondItem) {

        if (player.getMainHandItem().is(firstItem) && !player.getCooldowns().isOnCooldown(firstItem.getDefaultInstance())) {

            if (damageFirstItem && !removeFirstItem) player.getMainHandItem().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            else if (removeFirstItem && !damageFirstItem) player.getMainHandItem().consume(1, player);
            else sendUseBothItemsWarningMessage();

            player.awardStat(Stats.ITEM_USED.get(firstItem));
        }

        if (player.getOffhandItem().is(firstItem) && !player.getCooldowns().isOnCooldown(firstItem.getDefaultInstance())) {

            if (damageFirstItem && !removeFirstItem) player.getOffhandItem().hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            else if (removeFirstItem && !damageFirstItem) player.getOffhandItem().consume(1, player);
            else sendUseBothItemsWarningMessage();

            player.awardStat(Stats.ITEM_USED.get(firstItem));
        }

        if (player.getMainHandItem().is(secondItem) && !player.getCooldowns().isOnCooldown(secondItem.getDefaultInstance())) {

            if (damageSecondItem && !removeSecondItem) player.getMainHandItem().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            else if (removeSecondItem && !damageSecondItem) player.getMainHandItem().consume(1, player);
            else sendUseBothItemsWarningMessage();

            player.awardStat(Stats.ITEM_USED.get(secondItem));
        }

        if (player.getOffhandItem().is(secondItem) && !player.getCooldowns().isOnCooldown(secondItem.getDefaultInstance())) {

            if (damageSecondItem && !removeSecondItem) player.getOffhandItem().hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            else if (removeSecondItem && !damageSecondItem) player.getOffhandItem().consume(1, player);
            else sendUseBothItemsWarningMessage();

            player.awardStat(Stats.ITEM_USED.get(secondItem));
        }
    }

    private static void sendUseBothItemsWarningMessage() {
        Chrysalis.LOGGER.warn("Did not damage or remove either held item!");
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

    public static final DataComponentType<CustomData> SAVED_ENTITY_DATA_COMPONENT = CDataComponents.SAVED_ENTITY_DATA.get();

    public static CustomData getSavedEntityData(ItemStack itemStack) {
        return itemStack.getOrDefault(SAVED_ENTITY_DATA_COMPONENT, CustomData.EMPTY);
    }

    /**
     * Sets the equippable component for carved pumpkins and mob heads.
     **/

    public static Equippable setHeadEquippableComponent(Holder<SoundEvent> equipSound) {
        return Equippable.builder(EquipmentSlot.HEAD).setEquipSound(equipSound).setDamageOnHurt(false).build();
    }

    public static Equippable setHeadOverlayEquippableComponent(Holder<SoundEvent> equipSound, ResourceLocation cameraOverlayLocation) {
        return Equippable.builder(EquipmentSlot.HEAD).setEquipSound(equipSound).setDamageOnHurt(false).setCameraOverlay(cameraOverlayLocation).build();
    }

    public static Equippable setOffhandEquippableComponent(Holder<SoundEvent> equipSound, boolean damageOnHurt) {
        return Equippable.builder(EquipmentSlot.OFFHAND).setEquipSound(equipSound).setDamageOnHurt(damageOnHurt).setSwappable(false).build();
    }

    // endregion

    // region Item Entities

    /**
     * Sets the glow color entity data of an item entity.
     **/

    public static int getItemGlowColor(ItemEntity itemEntity) {
        return itemEntity.getEntityData().get(CRegistry.ITEM_GLOW_COLOR);
    }

    public static void setItemGlowColor(ItemEntity itemEntity, int glowColor) {
        itemEntity.getEntityData().set(CRegistry.ITEM_GLOW_COLOR, glowColor);
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

    public static void addUseOnEntityTooltip(List<Component> tooltip, Entity entity) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_used_on_entity", entity.getType().getDescription()).withStyle(ChatFormatting.GRAY));
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

    public static void addDamageBlockedTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_damage_blocked").withStyle(ChatFormatting.GRAY));
    }

    public static void addCanPlaceTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("item.canPlace").withStyle(ChatFormatting.GRAY));
    }

    public static void addCanBreakTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("item.canBreak").withStyle(ChatFormatting.GRAY));
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

    public static void addDescriptionTooltip(List<Component> tooltip, Item item, ChatFormatting color, boolean addSpace) {
        Component component = Component.translatable(item.getDescriptionId() + ".description").withStyle(color);
        if (addSpace) tooltip.add(CommonComponents.space().append(component));
        else tooltip.add(component);
    }

    public static void addItalicDescriptionTooltip(List<Component> tooltip, Item item, ChatFormatting color, boolean addSpace) {
        Component component = Component.translatable(item.getDescriptionId() + ".description").withStyle(ChatFormatting.ITALIC, color);
        if (addSpace) tooltip.add(CommonComponents.space().append(component));
        else tooltip.add(component);
    }

    public static Component addTooltipWithIcon(Component icon, Component tooltip) {
        return Component.translatable("gui.chrysalis.item.tooltip_with_icon", icon, tooltip);
    }

    public static Component addTooltipWithIconOnBothSides(Component icon, Component tooltip) {
        return Component.translatable("gui.chrysalis.item.tooltip_with_icon.both_sides", icon, tooltip, icon);
    }

    public static Component addVanillaDescriptionComponent(ItemStack itemStack, String modId, ChatFormatting color) {
        return CommonComponents.space().append(Component.translatable("item." + modId + "." + itemStack.getItem().getDescriptionId().replace("item.minecraft.", "") + ".description").withStyle(color));
    }

    public static void addModNameTooltip(Item.TooltipContext tooltipContext, ItemStack itemStack, String modId, MutableComponent modIcon, int color, CallbackInfoReturnable<List<Component>> cir) {
        if (tooltipContext.registries() != null && Objects.equals(itemStack.getItem().getCreatorModId(Objects.requireNonNull(tooltipContext.registries()), itemStack), modId) && !itemStack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP) && !cir.getReturnValue().isEmpty()) {
            cir.getReturnValue().add(CommonComponents.EMPTY);
            ComponentHelper.setIconsFont(modIcon, modId);
            Component tooltip = addTooltipWithIcon(modIcon, Component.translatable("mod." + modId).withStyle(style -> style.withFont(ComponentHelper.FIVE_FONT).withColor(color)));
            cir.getReturnValue().add(tooltip);
        }
    }

    // endregion

    // region Cooldowns

    /**
     * Allows for cooldowns to be set for items in specific item tags.
     **/

    public static void addCooldownToTag(ServerPlayer serverPlayer, TagKey<Item> itemTag, int cooldown) {
        List<Item> cooldownGroup = Chrysalis.registryAccess.lookupOrThrow(Registries.ITEM).stream().filter(codec -> codec.getDefaultInstance().is(itemTag)).toList();
        for (Item item : cooldownGroup) serverPlayer.getCooldowns().addCooldown(item.getDefaultInstance(), cooldown);
    }

    // endregion
}