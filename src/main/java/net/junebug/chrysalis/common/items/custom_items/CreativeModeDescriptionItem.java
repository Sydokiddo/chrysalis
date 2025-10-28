package net.junebug.chrysalis.common.items.custom_items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.junebug.chrysalis.util.helpers.ItemHelper;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CreativeModeDescriptionItem extends Item {

    public CreativeModeDescriptionItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a tooltip that only shows while in creative mode.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {

        if (tooltipFlag.isCreative()) {
            list.add(CommonComponents.EMPTY);
            ItemHelper.addItalicDescriptionTooltip(list, this, ChatFormatting.GRAY, false);
        }

        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }
}