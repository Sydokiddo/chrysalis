package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
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
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.base_classes.DebugUtilityItem;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;

public class TeleportToSpawnpointItem extends DebugUtilityItem {

    public TeleportToSpawnpointItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the teleport to spawnpoint item.
     **/

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Teleports the user to their spawnpoint when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

            serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

            if (serverPlayer.getRespawnPosition() != null) {

                serverPlayer.teleportTo(Objects.requireNonNull(serverPlayer.getRespawnPosition()).getX(), serverPlayer.getRespawnPosition().getY() + 1, serverPlayer.getRespawnPosition().getZ());
                level.gameEvent(GameEvent.TELEPORT, serverPlayer.position(), GameEvent.Context.of(serverPlayer));
                level.broadcastEntityEvent(serverPlayer, (byte) 46); // Teleporting Particles

                serverPlayer.playNotifySound(SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.teleport_to_spawnpoint.message.success"));
                serverPlayer.getCooldowns().addCooldown(itemStack, 60);

            } else {
                serverPlayer.playNotifySound(ChrysalisSoundEvents.TELEPORT_TO_SPAWNPOINT_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.teleport_to_spawnpoint.message.fail"));
            }
        }

        return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
    }
}