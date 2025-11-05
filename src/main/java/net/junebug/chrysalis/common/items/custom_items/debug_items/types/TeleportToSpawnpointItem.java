package net.junebug.chrysalis.common.items.custom_items.debug_items.types;

import net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes.TeleportingDebugUtilityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.junebug.chrysalis.util.helpers.ItemHelper;
import net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes.DebugUtilityItem;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class TeleportToSpawnpointItem extends TeleportingDebugUtilityItem {

    public TeleportToSpawnpointItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the teleport to spawnpoint item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Teleports the user to their spawnpoint when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResult use(Level level, Player player, @NotNull InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

            serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

            if (serverPlayer.getServer() != null && serverPlayer.getRespawnPosition() != null) {

                ServerLevel respawnDimension = serverPlayer.getServer().getLevel(serverPlayer.getRespawnDimension());
                BlockPos respawnPos = serverPlayer.getRespawnPosition();

                if (respawnDimension != null) {

                    serverPlayer.teleport(new TeleportTransition(respawnDimension, new Vec3(respawnPos.getX(), respawnPos.getY() + 1, respawnPos.getZ()), serverPlayer.getDeltaMovement(), serverPlayer.getYRot(), serverPlayer.getXRot(), Relative.union(Relative.DELTA, Relative.ROTATION), TeleportTransition.DO_NOTHING));
                    level.gameEvent(GameEvent.TELEPORT, serverPlayer.position(), GameEvent.Context.of(serverPlayer));
                    level.broadcastEntityEvent(serverPlayer, (byte) 46); // Teleporting Particles

                    serverPlayer.playNotifySound(SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("gui.chrysalis.teleport_to_spawnpoint.message.success"));
                    serverPlayer.getCooldowns().addCooldown(itemStack, 60);

                } else {
                    this.fail(serverPlayer);
                }

            } else {
                this.fail(serverPlayer);
            }
        }

        return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
    }

    private void fail(ServerPlayer serverPlayer) {
        this.fail(serverPlayer, Component.translatable("gui.chrysalis.teleport_to_spawnpoint.message.fail"));
    }
}