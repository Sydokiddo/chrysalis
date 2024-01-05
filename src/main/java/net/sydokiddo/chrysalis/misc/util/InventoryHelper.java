package net.sydokiddo.chrysalis.misc.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;
import static net.minecraft.nbt.Tag.TAG_LIST;

@SuppressWarnings("unused")
public class InventoryHelper {

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
}