package net.sydokiddo.chrysalis.common.items.custom_items.debug_items.shared_classes;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.common.misc.CGameRules;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class DebugUtilityItem extends Item {

    public DebugUtilityItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a description tooltip to any of the debug utility items.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addDescriptionTooltip(list, this, ChatFormatting.BLUE, true);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Debug utility items cannot be used to break blocks while in creative mode.
     **/

    @Override
    public boolean canAttackBlock(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public static void addParticlesAroundEntity(Entity entity, ParticleOptions particleType, int amount, double range) {

        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        for (int particleAmount = 0; particleAmount < amount; ++particleAmount) {
            double randomX = serverLevel.getRandom().nextGaussian() * 0.02D;
            double randomY = serverLevel.getRandom().nextGaussian() * 0.02D;
            double randomZ = serverLevel.getRandom().nextGaussian() * 0.02D;
            serverLevel.sendParticles(particleType, entity.getRandomX(range), entity.getRandomY() + 0.5D, entity.getRandomZ(range), 1, randomX, randomY, randomZ, 0.0D);
        }
    }

    public static void addSparkleParticles(Entity entity) {
        addParticlesAroundEntity(entity, ParticleTypes.HAPPY_VILLAGER, 10, 0.8D);
    }

    public static void sendFeedbackMessage(boolean requiresGameRule, ServerPlayer serverPlayer, Component message) {
        if (requiresGameRule && !serverPlayer.serverLevel().getGameRules().getBoolean(CGameRules.RULE_SEND_DEBUG_UTILITY_FEEDBACK)) return;
        serverPlayer.sendSystemMessage(message);
    }
}