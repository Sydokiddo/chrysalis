package net.sydokiddo.chrysalis.common.items.custom_items.debug_items.types;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.shared_classes.DebugUtilityItem;
import net.sydokiddo.chrysalis.common.misc.CSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class GiveResistanceItem extends DebugUtilityItem {

    public GiveResistanceItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the give resistance item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Gives the user infinite resistance when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResult use(Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

            serverPlayer.playNotifySound(CSoundEvents.GIVE_RESISTANCE_USE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            addParticlesAroundEntity(serverPlayer, new BlockParticleOption(ParticleTypes.BLOCK, Blocks.IRON_BLOCK.defaultBlockState()), 10, 1.0D);

            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
            Holder<MobEffect> resistance = MobEffects.DAMAGE_RESISTANCE;
            serverPlayer.addEffect(new MobEffectInstance(resistance, -1, 255, false, false, true));
            DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("commands.effect.give.success.single", resistance.value().getDisplayName(), serverPlayer.getName().getString()));
        }

        return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(interactionHand));
    }
}