package net.junebug.chrysalis.common.items.custom_items.debug_items.types;

import net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes.DebugUtilityItem;
import net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes.TeleportingDebugUtilityItem;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.util.helpers.ComponentHelper;
import net.junebug.chrysalis.util.helpers.EntityHelper;
import net.junebug.chrysalis.util.helpers.ItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class TeleportToDimensionItem extends TeleportingDebugUtilityItem {

    private final ResourceKey<Level> dimension;

    public TeleportToDimensionItem(Properties properties, ResourceKey<Level> dimension) {
        super(properties);
        this.dimension = dimension;
    }

    private Component getDimensionComponent() {
        return ComponentHelper.getDimensionComponent(this.dimension.location().toString());
    }

    /**
     * Adds a custom tooltip to the teleport to dimension item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        list.add(CommonComponents.space().append(Component.translatable("item.chrysalis.teleport_to_dimension.description", this.getDimensionComponent().copy().withStyle(ChatFormatting.BLUE))).withStyle(ChatFormatting.BLUE));
    }

    /**
     * Teleports the user to the item's specified dimension when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResult use(Level level, Player player, @NotNull InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

            serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

            if (serverPlayer.getServer() != null) {

                ServerLevel teleportDimension = serverPlayer.getServer().getLevel(this.dimension);

                if (teleportDimension != null && !EntityHelper.isInDimension(serverPlayer, this.dimension)) {

                    double teleportScale = DimensionType.getTeleportationScale(serverPlayer.level().dimensionType(), teleportDimension.dimensionType());
                    BlockPos teleportPos = serverPlayer.level().getWorldBorder().clampToBounds(serverPlayer.getX() * teleportScale, serverPlayer.getY() + 1, serverPlayer.getZ() * teleportScale);

                    serverPlayer.teleport(new TeleportTransition(teleportDimension, new Vec3(teleportPos.getX() * teleportScale, teleportPos.getY(), teleportPos.getZ() * teleportScale), serverPlayer.getDeltaMovement(), serverPlayer.getYRot(), serverPlayer.getXRot(), Relative.union(Relative.DELTA, Relative.ROTATION), TeleportTransition.PLAY_PORTAL_SOUND));
                    level.gameEvent(GameEvent.TELEPORT, serverPlayer.position(), GameEvent.Context.of(serverPlayer));
                    level.broadcastEntityEvent(serverPlayer, (byte) 46); // Teleporting Particles

                    serverPlayer.playNotifySound(SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("gui.chrysalis.teleport_to_dimension.message.success", this.getDimensionComponent()));
                    ItemHelper.addCooldownToTag(serverPlayer, CTags.TELEPORT_TO_DIMENSION_ITEMS, 60);

                } else {
                    this.fail(serverPlayer, Component.translatable("gui.chrysalis.teleport_to_dimension.message.fail_already_in_dimension", this.getDimensionComponent()));
                }

            } else {
                this.fail(serverPlayer, Component.translatable("gui.chrysalis.teleport_to_dimension.message.fail_cannot_find_dimension"));
            }
        }

        return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
    }
}