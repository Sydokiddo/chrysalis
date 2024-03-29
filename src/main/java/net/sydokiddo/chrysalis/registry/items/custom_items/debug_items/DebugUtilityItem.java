package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.items.custom_items.EnchantedGlintItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class DebugUtilityItem extends EnchantedGlintItem {

    public DebugUtilityItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a description tooltip to any of the Debug Utility items.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        tooltip.add(CommonComponents.space().append(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.BLUE)));
    }
}