package net.sydokiddo.chrysalis.misc.util.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;
import static net.minecraft.nbt.Tag.TAG_LIST;

@SuppressWarnings("unused")
public class ItemHelper {

    /**
     * Checks to see if a block entity as an item is empty when in the player's inventory.
     **/

    public static boolean containerIsEmpty(ItemStack itemStack) {

        String blockEntityTagString = "BlockEntityTag";
        String itemsString = "Items";
        CompoundTag compoundTag = itemStack.getTag();

        if (compoundTag == null || !compoundTag.contains(blockEntityTagString, TAG_COMPOUND)) return true;

        CompoundTag blockEntityTag = compoundTag.getCompound(blockEntityTagString);
        return !blockEntityTag.contains(itemsString, TAG_LIST) || blockEntityTag.getList(itemsString, TAG_COMPOUND).isEmpty();
    }

    /**
     * Checks for a specific item in the player's inventory.
     **/

    public static boolean hasItemInInventory(Item item, Player player) {
        for (int slots = 0; slots <= 35; slots++) {
            if (player.getInventory().getItem(slots).is(item) || player.getOffhandItem().is(item) || player.getItemBySlot(EquipmentSlot.HEAD).is(item) ||
            player.getItemBySlot(EquipmentSlot.CHEST).is(item) || player.getItemBySlot(EquipmentSlot.LEGS).is(item) || player.getItemBySlot(EquipmentSlot.FEET).is(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a food item's saturation amount.
     **/

    public static BigDecimal getFoodSaturation(ItemStack itemStack) {
        float saturationAmount = Objects.requireNonNull(itemStack.getItem().getFoodProperties()).getNutrition() * Objects.requireNonNull(itemStack.getItem().getFoodProperties()).getSaturationModifier() * 2.0F;
        return new BigDecimal(saturationAmount).setScale(1, RoundingMode.DOWN);
    }

    /**
     * Checks to see if an item has an armor trim on it.
     **/

    public static boolean hasArmorTrim(ItemStack itemStack) {
        return itemStack.getTag() != null && itemStack.getTag().contains(ArmorTrim.TAG_TRIM_ID);
    }

    public static boolean hasEnchantmentOrTrim(ItemStack itemStack) {
        return (itemStack.isEnchanted() || hasArmorTrim(itemStack));
    }

    // region Custom Item Tooltips

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

        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.dimension",
                Component.translatable("dimension." + registryKey + "." + registryPath).withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.BLUE)));
    }

    public static void addSpaceOnTooltipIfEnchantedOrTrimmed(ItemStack itemStack, List<Component> tooltip) {
        if (hasEnchantmentOrTrim(itemStack)) tooltip.add(CommonComponents.EMPTY);
    }

    public static void addNullTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.none").withStyle(ChatFormatting.BLUE)));
    }

    // endregion
}