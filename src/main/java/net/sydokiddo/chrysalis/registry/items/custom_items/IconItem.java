package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class IconItem extends Item {

    /**
     * The base class for icon items used for creative mode tabs, which are removed from the player's inventory if they somehow manage to get one.
     **/

    public IconItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        itemStack.setCount(0);
    }
}