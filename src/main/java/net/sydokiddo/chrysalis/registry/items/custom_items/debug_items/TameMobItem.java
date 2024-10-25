package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class TameMobItem extends DebugUtilityItem {

    public TameMobItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Automatically tames any tamable mob when the mob is right-clicked with the item.
     **/

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {

        if (livingEntity instanceof TamableAnimal tamableAnimal && !tamableAnimal.isTame()) {

            if (!livingEntity.level().isClientSide()) {
                tamableAnimal.tame(player);
                playTameEvents(player, tamableAnimal);
                player.awardStat(Stats.ITEM_USED.get(this));
            }

            return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(interactionHand));
        }

        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }

    public static void playTameEvents(Player player, LivingEntity tamedMob) {

        if (!(player instanceof ServerPlayer serverPlayer)) return;

        serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        serverPlayer.playNotifySound(ChrysalisSoundEvents.TAME_MOB_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (tamedMob.level() instanceof ServerLevel serverLevel) {
            for (int particleAmount = 0; particleAmount < 7; ++particleAmount) {
                double random = tamedMob.level().getRandom().nextGaussian() * 0.02D;
                serverLevel.sendParticles(ParticleTypes.HEART, tamedMob.getRandomX(1.0D), tamedMob.getRandomY() + 0.5D, tamedMob.getRandomZ(1.0D), 1, 0.0, random, random, random);
            }
        }

        serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.tame_mob_message", tamedMob.getName().getString()));
    }
}