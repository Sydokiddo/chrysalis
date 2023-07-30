package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.registry.items.custom_items.EnchantmentGlintItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class FillHungerItem extends EnchantmentGlintItem {

    public FillHungerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(this.getDisplayName().withStyle(ChatFormatting.GRAY));
    }

    private MutableComponent getDisplayName() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }

    /**
     * Fills the user's hunger bar to the max value when right-clicked with.
     **/

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (player.getFoodData().needsFood()) {
            if (!level.isClientSide) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                player.playNotifySound(SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0f, 1.0f);
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
                player.getFoodData().setFoodLevel(20);
                player.getFoodData().setSaturation(5.0F);
                serverPlayer.sendSystemMessage(Component.translatable("item.chrysalis.fill_hunger_message"));
            }
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }
        return InteractionResultHolder.pass(itemStack);
    }
}