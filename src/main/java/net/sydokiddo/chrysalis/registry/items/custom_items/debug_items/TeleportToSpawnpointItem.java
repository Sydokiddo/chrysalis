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
import java.util.Objects;

public class TeleportToSpawnpointItem extends DebugUtilityItem {

    public TeleportToSpawnpointItem(Properties properties) {
        super(properties);
    }

    /**
     * Teleports the user to their spawnpoint when right-clicked with.
     **/

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (!level.isClientSide) {

            player.gameEvent(GameEvent.ITEM_INTERACT_START);

            if (player instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.getRespawnPosition() != null) {
                    serverPlayer.teleportTo(Objects.requireNonNull(serverPlayer.getRespawnPosition()).getX(), serverPlayer.getRespawnPosition().getY() + 1, serverPlayer.getRespawnPosition().getZ());
                    player.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
                    serverPlayer.sendSystemMessage(Component.translatable("item.chrysalis.teleport_to_spawnpoint_message"));
                    player.getCooldowns().addCooldown(this, 60);
                } else {
                    player.playNotifySound(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0f, 1.0f);
                    serverPlayer.sendSystemMessage(Component.translatable("item.chrysalis.teleport_to_spawnpoint_failed_message"));
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
    }
}