package net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets;

import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;

public class ElytraEnchantTarget extends EnchantmentCategoryMixin {

    @Override
    public boolean canEnchant(Item other) {
        return other instanceof ElytraItem;
    }
}