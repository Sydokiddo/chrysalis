package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.base_classes;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.items.custom_items.EnchantedGlintItem;
import java.util.List;

public class DebugUtilityItem extends EnchantedGlintItem {

    public DebugUtilityItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a description tooltip to any of the Debug Utility items.
     **/

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(CommonComponents.space().append(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.BLUE)));
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Debug Utility items cannot be used to break blocks while in Creative Mode.
     **/

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public static void addParticlesAroundEntity(Entity entity, ParticleOptions particleType, int amount, double range) {

        if (!(entity.level() instanceof ServerLevel serverLevel)) return;

        for (int particleAmount = 0; particleAmount < amount; ++particleAmount) {
            double random = serverLevel.getRandom().nextGaussian() * 0.02D;
            serverLevel.sendParticles(particleType, entity.getRandomX(range), entity.getRandomY() + 0.5D, entity.getRandomZ(range), 1, 0.0D, random, random, random);
        }
    }
}