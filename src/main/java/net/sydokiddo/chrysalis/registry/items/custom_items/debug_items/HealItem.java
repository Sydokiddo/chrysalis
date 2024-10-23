package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class HealItem extends DebugUtilityItem {

    public HealItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Sets the user to full health when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand interactionHand) {

        if (player instanceof ServerPlayer serverPlayer && serverPlayer.getHealth() < serverPlayer.getMaxHealth()) {

            if (!level.isClientSide()) {
                serverPlayer.playNotifySound(ChrysalisSoundEvents.HEAL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                serverPlayer.awardStat(Stats.ITEM_USED.get(this));
                serverPlayer.setHealth(serverPlayer.getMaxHealth());
                serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.heal_message", serverPlayer.getName().getString()));
            }

            return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(player.getItemInHand(interactionHand));
        }

        return super.use(level, player, interactionHand);
    }
}