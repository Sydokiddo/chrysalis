package net.sydokiddo.chrysalis.common.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.base_classes.DebugUtilityItem;
import net.sydokiddo.chrysalis.common.misc.CSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class RideMobItem extends DebugUtilityItem {

    public RideMobItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the ride mob item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Automatically mounts any mob when the mob is right-clicked with the item.
     **/

    public static InteractionResult doInteraction(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {

        if (!player.isShiftKeyDown()) {

            if (player instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide()) {

                serverPlayer.setXRot(livingEntity.getXRot());
                serverPlayer.setYRot(livingEntity.getYRot());
                serverPlayer.startRiding(livingEntity);

                serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                serverPlayer.playNotifySound(CSoundEvents.RIDE_MOB_USE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("commands.ride.mount.success", serverPlayer.getName().getString(), livingEntity.getName().getString()));
            }

            return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(interactionHand));
        }

        return InteractionResult.PASS;
    }
}