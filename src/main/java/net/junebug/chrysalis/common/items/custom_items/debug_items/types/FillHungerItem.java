package net.junebug.chrysalis.common.items.custom_items.debug_items.types;

import net.junebug.chrysalis.util.helpers.EntityHelper;
import net.junebug.chrysalis.util.helpers.ParticleHelper;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.junebug.chrysalis.util.helpers.ItemHelper;
import net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes.DebugUtilityItem;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class FillHungerItem extends DebugUtilityItem {

    public FillHungerItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the fill hunger item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Fills the user's hunger bar to the maximum value when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {

        if (player.getFoodData().needsFood()) {

            if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

                EntityHelper.playItemUseNotifySound(serverPlayer, CSoundEvents.FILL_HUNGER_USE.get());
                ParticleHelper.emitParticlesAroundEntity(serverPlayer, new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.COOKED_BEEF)), 1.0D, 10);

                serverPlayer.awardStat(Stats.ITEM_USED.get(this));
                serverPlayer.getFoodData().setFoodLevel(20);
                serverPlayer.getFoodData().setSaturation(5.0F);
                DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("gui.chrysalis.fill_hunger.message", serverPlayer.getName().getString()));
            }

            return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(interactionHand));
        }

        return super.use(level, player, interactionHand);
    }
}