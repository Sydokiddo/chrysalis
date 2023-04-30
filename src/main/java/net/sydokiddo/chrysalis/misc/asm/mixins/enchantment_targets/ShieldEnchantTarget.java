package net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;

@SuppressWarnings("ALL")
public class ShieldEnchantTarget extends EnchantmentCategoryMixin {

    @Override
    public boolean canEnchant(Item item) {
        return item instanceof ShieldItem;
    }
}