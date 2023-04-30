package net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpyglassItem;

@SuppressWarnings("ALL")
public class SpyglassEnchantTarget extends EnchantmentCategoryMixin {

    @Override
    public boolean canEnchant(Item item) {
        return item instanceof SpyglassItem;
    }
}