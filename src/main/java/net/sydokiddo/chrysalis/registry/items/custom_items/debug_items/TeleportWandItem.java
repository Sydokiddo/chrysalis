package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class TeleportWandItem extends DebugUtilityItem {

    public TeleportWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Teleports the player to the position they are looking at when right-clicked with the Teleport Wand.
     **/

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

            serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

            HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player, entity -> !entity.isSpectator() && entity.isPickable(), Minecraft.getInstance().gameRenderer.getRenderDistance() * 4.0F);
            Vec3 teleportToPos = new Vec3(hitResult.getLocation().x(), hitResult.getLocation().y(), hitResult.getLocation().z());

            serverPlayer.teleportTo(teleportToPos.x(), teleportToPos.y(), teleportToPos.z());
            if (serverPlayer.isInWall()) serverPlayer.moveTo(Vec3.atCenterOf(serverPlayer.getOnPos().above()));

            if (serverPlayer.isPassenger()) serverPlayer.stopRiding();
            serverPlayer.resetFallDistance();
            serverPlayer.resetCurrentImpulseContext();

            serverPlayer.playNotifySound(SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
            level.gameEvent(GameEvent.TELEPORT, serverPlayer.position(), GameEvent.Context.of(serverPlayer));
            level.broadcastEntityEvent(serverPlayer, (byte) 46); // Teleporting Particles

            serverPlayer.getCooldowns().addCooldown(this, 60);
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
    }
}