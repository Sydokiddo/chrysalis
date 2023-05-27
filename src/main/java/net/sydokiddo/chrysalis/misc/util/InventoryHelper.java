package net.sydokiddo.chrysalis.misc.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("ALL")
public class InventoryHelper {

    private static final int TAG_LIST = 9;
    private static final int TAG_COMPOUND = 10;

    public static boolean containerIsEmpty(ItemStack stack) {

        CompoundTag tag = stack.getTag();

        if (tag == null || !tag.contains("BlockEntityTag", TAG_COMPOUND))
            return true;

        CompoundTag blockEntityTag = tag.getCompound("BlockEntityTag");
        return !blockEntityTag.contains("Items", TAG_LIST) || blockEntityTag.getList("Items", TAG_COMPOUND).isEmpty();
    }
}