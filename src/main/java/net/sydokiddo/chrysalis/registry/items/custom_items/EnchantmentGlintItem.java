package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EnchantmentGlintItem extends Item {

    public EnchantmentGlintItem(Properties properties) {
        super(properties);
    }

    /**
     * Any items that extend this class will automatically display the enchantment glint on them.
     **/

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}