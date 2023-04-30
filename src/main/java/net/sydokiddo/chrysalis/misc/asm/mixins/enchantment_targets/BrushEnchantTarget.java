package net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets;

import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;

@SuppressWarnings("ALL")
public class BrushEnchantTarget extends EnchantmentCategoryMixin {

    @Override
    public boolean canEnchant(Item item) {
        return item instanceof BrushItem;
    }
}