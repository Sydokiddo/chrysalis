package net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets;

import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;

@SuppressWarnings("ALL")
public class ElytraEnchantTarget extends EnchantmentCategoryMixin {

    @Override
    public boolean canEnchant(Item item) {
        return item instanceof ElytraItem;
    }
}