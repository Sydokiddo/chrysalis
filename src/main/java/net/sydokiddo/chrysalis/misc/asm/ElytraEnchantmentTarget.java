package net.sydokiddo.chrysalis.misc.asm;

import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.sydokiddo.chrysalis.mixin.util.EnchantmentCategoryMixin;

@SuppressWarnings("ALL")
public class ElytraEnchantmentTarget extends EnchantmentCategoryMixin {

    @Override
    public boolean canEnchant(Item item) {
        return item instanceof ElytraItem;
    }
}