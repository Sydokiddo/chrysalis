package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class HealItem extends DebugUtilityItem {

    public HealItem(Properties properties) {
        super(properties);
    }

    /**
     * Sets the user to full health when right-clicked with.
     **/

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (player.getHealth() < player.getMaxHealth()) {
            if (!level.isClientSide) {
                player.playNotifySound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
                player.setHealth(player.getMaxHealth());
                player.sendSystemMessage(Component.translatable("item.chrysalis.heal_message"));
            }
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemStack);
    }
}