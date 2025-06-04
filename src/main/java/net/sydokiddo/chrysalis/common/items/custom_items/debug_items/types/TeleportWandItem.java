package net.sydokiddo.chrysalis.common.items.custom_items.debug_items.types;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.common.items.custom_items.CustomCrosshairItem;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.shared_classes.DebugUtilityItem;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class TeleportWandItem extends DebugUtilityItem implements CustomCrosshairItem {

    public TeleportWandItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the teleport wand item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Teleports the player to the position they are looking at when right-clicked with the Teleport Wand.
     **/

    private HitResult getHitResult(Player player) {
        return ProjectileUtil.getHitResultOnViewVector(player, entity -> !entity.isSpectator(), Minecraft.getInstance().gameRenderer.getRenderDistance() * 4.0F);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {

        if (this.getHitResult(player).getType() != HitResult.Type.BLOCK) return super.use(level, player, interactionHand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

            serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

            Vec3 teleportToPos = new Vec3(this.getHitResult(player).getLocation().x(), this.getHitResult(player).getLocation().y(), this.getHitResult(player).getLocation().z());
            serverPlayer.teleportTo(teleportToPos.x(), teleportToPos.y() + 1, teleportToPos.z());
            if (serverPlayer.isInWall()) serverPlayer.moveTo(Vec3.atCenterOf(serverPlayer.getOnPos().above()));

            if (serverPlayer.isPassenger()) serverPlayer.stopRiding();
            serverPlayer.resetFallDistance();
            serverPlayer.resetCurrentImpulseContext();

            serverPlayer.playNotifySound(SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
            level.gameEvent(GameEvent.TELEPORT, serverPlayer.position(), GameEvent.Context.of(serverPlayer));
            level.broadcastEntityEvent(serverPlayer, (byte) 46); // Teleporting Particles
        }

        return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(interactionHand));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldDisplayCrosshair(Player player) {
        return this.getHitResult(player).getType() == HitResult.Type.BLOCK;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getCrosshairTextureLocation() {
        return Chrysalis.resourceLocationId("hud/crosshair/teleport_wand");
    }
}