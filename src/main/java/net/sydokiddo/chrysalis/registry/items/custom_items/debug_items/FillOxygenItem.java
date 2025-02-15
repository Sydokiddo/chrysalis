package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.base_classes.DebugUtilityItem;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class FillOxygenItem extends DebugUtilityItem {

    public FillOxygenItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the fill oxygen item.
     **/

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Fills the user's oxygen bar to the maximum value when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand interactionHand) {

        if (player.getAirSupply() < player.getMaxAirSupply()) {

            if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

                serverPlayer.playNotifySound(ChrysalisSoundEvents.FILL_OXYGEN_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                addParticlesAroundEntity(serverPlayer, ParticleTypes.BUBBLE, 10, 1.0D);

                serverPlayer.awardStat(Stats.ITEM_USED.get(this));
                serverPlayer.setAirSupply(serverPlayer.getMaxAirSupply());
                serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.fill_oxygen.message", serverPlayer.getName().getString()));
            }

            return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(interactionHand));
        }

        return super.use(level, player, interactionHand);
    }
}