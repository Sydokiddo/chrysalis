package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class TameMobItem extends DebugUtilityItem {

    public TameMobItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        ChrysalisRegistry.addUseTooltip(tooltip);
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
        if (livingEntity instanceof TamableAnimal tamableAnimal) {
            tamableAnimal.tame(player);
            playTameEvents(player, tamableAnimal);
            return InteractionResult.SUCCESS;
        } else if (livingEntity instanceof AbstractHorse abstractHorse) {
            abstractHorse.tameWithName(player);
            playTameEvents(player, abstractHorse);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }

    private void playTameEvents(Player player, LivingEntity tamedMob) {

        player.gameEvent(GameEvent.ITEM_INTERACT_START);
        player.playNotifySound(SoundEvents.WOLF_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);

        for(int i = 0; i < 7; ++i) {
            double d = tamedMob.level().random.nextGaussian() * 0.02D;
            double e = tamedMob.level().random.nextGaussian() * 0.02D;
            double f = tamedMob.level().random.nextGaussian() * 0.02D;
            tamedMob.level().addParticle(ParticleTypes.HEART, tamedMob.getRandomX(1.0D), tamedMob.getRandomY() + 0.5D, tamedMob.getRandomZ(1.0D), d, e, f);
        }

        player.sendSystemMessage(Component.translatable("item.chrysalis.tame_mob_message", tamedMob.getName().getString()));
    }
}