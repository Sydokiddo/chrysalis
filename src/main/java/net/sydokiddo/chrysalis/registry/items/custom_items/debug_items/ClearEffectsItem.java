package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ClearEffectsItem extends DebugUtilityItem {

    public ClearEffectsItem(Properties properties) {
        super(properties);
    }

    /**
     * Clears the user's status effects when right-clicked with.
     **/

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!player.getActiveEffects().isEmpty()) {
            if (!level.isClientSide) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                player.playNotifySound(SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0f, 1.0f);
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
                player.removeAllEffects();
                serverPlayer.sendSystemMessage(Component.translatable("item.chrysalis.clear_effects_message"));
            }
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemStack);
    }
}