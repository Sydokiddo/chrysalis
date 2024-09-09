package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class GiveResistanceItem extends DebugUtilityItem {

    public GiveResistanceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Gives the user infinite resistance when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        if (!level.isClientSide()) {

            player.playNotifySound(ChrysalisSoundEvents.GIVE_RESISTANCE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
            player.awardStat(Stats.ITEM_USED.get(this));

            Holder<MobEffect> resistance = MobEffects.DAMAGE_RESISTANCE;
            player.addEffect(new MobEffectInstance(resistance, -1, 255, false, false, true));
            player.sendSystemMessage(Component.translatable("commands.effect.give.success.single", resistance.value().getDisplayName(), player.getName().getString()));
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
    }
}