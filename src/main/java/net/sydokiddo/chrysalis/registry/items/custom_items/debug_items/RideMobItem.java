package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class RideMobItem extends DebugUtilityItem {

    public RideMobItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Automatically mounts any mob when the mob is right-clicked with the item.
     **/

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {

        if (!player.level().isClientSide() && !player.isShiftKeyDown()) {

            player.setXRot(livingEntity.getXRot());
            player.setYRot(livingEntity.getYRot());
            player.startRiding(livingEntity);

            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            player.playNotifySound(ChrysalisSoundEvents.RIDE_MOB_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            player.awardStat(Stats.ITEM_USED.get(this));

            player.sendSystemMessage(Component.translatable("commands.ride.mount.success", player.getName().getString(), livingEntity.getName().getString()));
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }
}