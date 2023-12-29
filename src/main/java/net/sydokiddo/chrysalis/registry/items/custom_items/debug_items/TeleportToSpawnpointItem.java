package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public class TeleportToSpawnpointItem extends DebugUtilityItem {

    public TeleportToSpawnpointItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        RegistryHelpers.addUseTooltip(tooltip);
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    /**
     * Teleports the user to their spawnpoint when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (!level.isClientSide()) {

            player.gameEvent(GameEvent.ITEM_INTERACT_START);
            player.awardStat(Stats.ITEM_USED.get(this));

            if (player instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.getRespawnPosition() != null) {
                    serverPlayer.teleportTo(Objects.requireNonNull(serverPlayer.getRespawnPosition()).getX(), serverPlayer.getRespawnPosition().getY() + 1, serverPlayer.getRespawnPosition().getZ());
                    player.playNotifySound(SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.teleport_to_spawnpoint_message"));
                    player.getCooldowns().addCooldown(this, 60);
                } else {
                    player.playNotifySound(ChrysalisSoundEvents.TELEPORT_TO_SPAWNPOINT_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
                    serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.teleport_to_spawnpoint_failed_message"));
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
    }
}