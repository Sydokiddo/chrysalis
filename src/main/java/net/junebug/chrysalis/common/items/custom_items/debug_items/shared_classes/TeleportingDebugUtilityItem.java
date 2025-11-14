package net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes;

import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

public class TeleportingDebugUtilityItem extends DebugUtilityItem {

    /**
     * The base class that teleporting debug utility items use.
     **/

    public TeleportingDebugUtilityItem(Properties properties) {
        super(properties);
    }

    public void fail(ServerPlayer serverPlayer, Component component) {
        serverPlayer.playNotifySound(CSoundEvents.TELEPORTING_ITEM_FAIL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        DebugUtilityItem.sendFeedbackMessage(false, serverPlayer, component.copy().withStyle(ChatFormatting.RED));
    }
}