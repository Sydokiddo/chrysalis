package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class GiveResistanceItem extends DebugUtilityItem {

    public GiveResistanceItem(Properties properties) {
        super(properties);
    }

    /**
     * Gives the user infinite resistance when right-clicked with.
     **/

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            player.playNotifySound(SoundEvents.IRON_GOLEM_REPAIR, SoundSource.PLAYERS, 1.0f, 1.0f);
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 255, false, false, true));
            serverPlayer.sendSystemMessage(Component.translatable("item.chrysalis.give_resistance_message"));
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}