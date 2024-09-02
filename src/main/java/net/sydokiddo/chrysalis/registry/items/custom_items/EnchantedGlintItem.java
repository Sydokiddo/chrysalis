package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

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

    public abstract void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag);
}