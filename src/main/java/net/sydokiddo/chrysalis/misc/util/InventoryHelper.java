package net.sydokiddo.chrysalis.misc.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class InventoryHelper {

    /**
     * Checks to see if a block entity as an item is empty when in the user's inventory.
     **/

    public static boolean containerIsEmpty(ItemStack stack) {

        String blockEntityTagString = "BlockEntityTag";
        String itemsString = "Items";
        int TAG_COMPOUND = 10;
        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains(blockEntityTagString, TAG_COMPOUND)) return true;

        CompoundTag blockEntityTag = tag.getCompound(blockEntityTagString);
        return !blockEntityTag.contains(itemsString, 9) || blockEntityTag.getList(itemsString, TAG_COMPOUND).isEmpty();
    }
}