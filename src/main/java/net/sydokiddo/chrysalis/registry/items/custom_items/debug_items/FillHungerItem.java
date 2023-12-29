package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
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

public class FillHungerItem extends DebugUtilityItem {

    public FillHungerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        RegistryHelpers.addUseTooltip(tooltip);
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    /**
     * Fills the user's hunger bar to the max value when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (player.getFoodData().needsFood()) {
            if (!level.isClientSide()) {
                player.playNotifySound(ChrysalisSoundEvents.FILL_HUNGER_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
                player.getFoodData().setFoodLevel(20);
                player.getFoodData().setSaturation(5.0F);
                player.sendSystemMessage(Component.translatable("gui.chrysalis.fill_hunger_message", player.getName().getString()));
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemStack);
    }
}