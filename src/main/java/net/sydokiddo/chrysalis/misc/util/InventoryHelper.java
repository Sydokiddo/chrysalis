package net.sydokiddo.chrysalis.misc.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("all")
public class InventoryHelper {

    private static final int TAG_COMPOUND = 10;

    /**
     * Checks to see if a block entity as an item is empty when in the user's inventory.
     **/

    public static boolean containerIsEmpty(ItemStack stack) {

        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains("BlockEntityTag", TAG_COMPOUND)) return true;

        CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
        return !blockEntityTag.contains("Items", 9) || blockEntityTag.getList("Items", TAG_COMPOUND).isEmpty();
    }
}