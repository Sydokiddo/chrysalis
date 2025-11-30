package net.junebug.chrysalis.common.items.custom_items.debug_items.types;

import net.junebug.chrysalis.common.entities.custom_entities.mobs.key_golem.KeyGolem;
import net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes.DebugUtilityItem;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.util.helpers.ItemHelper;
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
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class PickUpMobItem extends DebugUtilityItem {

    public PickUpMobItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the pick up mob item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Automatically picks up any mob when the mob is right-clicked with the item.
     **/

    public static InteractionResult doInteraction(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {

        if (!player.isShiftKeyDown()) {

            if (player instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide() && !livingEntity.isRemoved() && !livingEntity.isDeadOrDying()) {

                serverPlayer.gameEvent(GameEvent.ENTITY_INTERACT);
                serverPlayer.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

                if (livingEntity instanceof KeyGolem keyGolem && keyGolem.isFake()) {
                    keyGolem.despawnFakeKeyGolem();
                    return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(player.getItemInHand(interactionHand));
                }

                livingEntity.setXRot(player.getXRot());
                livingEntity.setYRot(player.getYRot());
                livingEntity.startRiding(player, true);

                serverPlayer.playNotifySound(CSoundEvents.PICK_UP_MOB_USE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("commands.ride.mount.success", livingEntity.getName().getString(), serverPlayer.getName().getString()));
            }

            return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(interactionHand));
        }

        return InteractionResult.PASS;
    }
}