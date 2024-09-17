package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.items.custom_items.EnchantedGlintItem;
import java.util.List;

public class DebugUtilityItem extends EnchantedGlintItem {

    public DebugUtilityItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a description tooltip to any of the Debug Utility items.
     **/

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(CommonComponents.space().append(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.BLUE)));
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Debug Utility items cannot be used to break blocks while in Creative Mode.
     **/

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }
}