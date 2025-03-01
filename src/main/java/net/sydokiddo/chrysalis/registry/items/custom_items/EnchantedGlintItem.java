package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class EnchantedGlintItem extends Item {

    /**
     * Any item that extends this class will automatically display the enchanted glint on it.
     **/

    public EnchantedGlintItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return true;
    }
}