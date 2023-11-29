package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class GiveResistanceItem extends DebugUtilityItem {

    public GiveResistanceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        RegistryHelpers.addUseTooltip(tooltip);
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    /**
     * Gives the user infinite resistance when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide()) {
            player.playNotifySound(ChrysalisSoundEvents.GIVE_RESISTANCE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 255, false, false, true));
            player.sendSystemMessage(Component.translatable("gui.chrysalis.give_resistance_message", player.getName().getString()));
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
    }
}