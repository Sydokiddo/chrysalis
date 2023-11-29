package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
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

public class TameMobItem extends DebugUtilityItem {

    public TameMobItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        RegistryHelpers.addUseTooltip(tooltip);
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    /**
     * Automatically tames any tamable mob when the mob is right-clicked with the item.
     **/

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
        if (!livingEntity.level().isClientSide() && livingEntity instanceof TamableAnimal tamableAnimal && !tamableAnimal.isTame()) {
            tamableAnimal.tame(player);
            playTameEvents(player, tamableAnimal);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }

    public static void playTameEvents(Player player, LivingEntity tamedMob) {

        player.gameEvent(GameEvent.ITEM_INTERACT_START);
        player.playNotifySound(ChrysalisSoundEvents.TAME_MOB_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (tamedMob.level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 7; ++i) {
                double d = tamedMob.level().getRandom().nextGaussian() * 0.02D;
                double e = tamedMob.level().getRandom().nextGaussian() * 0.02D;
                double f = tamedMob.level().getRandom().nextGaussian() * 0.02D;
                serverLevel.sendParticles(ParticleTypes.HEART, tamedMob.getRandomX(1.0D), tamedMob.getRandomY() + 0.5D, tamedMob.getRandomZ(1.0D), 1, 0.0, d, e, f);
            }
        }
        player.sendSystemMessage(Component.translatable("gui.chrysalis.tame_mob_message", tamedMob.getName().getString()));
    }
}