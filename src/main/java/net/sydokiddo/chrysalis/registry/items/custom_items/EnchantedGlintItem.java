package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class EnchantedGlintItem extends Item {

    public EnchantedGlintItem(Properties properties) {
        super(properties);
    }

    /**
     * Any items that extend this class will automatically display the enchanted glint on them.
     **/

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}