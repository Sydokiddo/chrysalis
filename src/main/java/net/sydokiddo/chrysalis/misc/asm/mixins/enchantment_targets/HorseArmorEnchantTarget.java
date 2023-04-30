package net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets;

import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.Item;

@SuppressWarnings("ALL")
public class HorseArmorEnchantTarget extends EnchantmentCategoryMixin {

    @Override
    public boolean canEnchant(Item item) {
        return item instanceof HorseArmorItem;
    }
}