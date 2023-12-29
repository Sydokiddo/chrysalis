package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisDamageSources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class KillWandItem extends DebugUtilityItem {

    public KillWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        RegistryHelpers.addAttackTooltip(tooltip);
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    /**
     * Any mob that is hurt with this item will automatically deal the max integer amount of damage to it.
     **/

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity self) {
        if (!target.isInvulnerable()) {

            target.hurt(target.damageSources().source(ChrysalisDamageSources.KILL_WAND, target), Float.MAX_VALUE);

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(self.level());
            assert lightningBolt != null;

            lightningBolt.moveTo(Vec3.atBottomCenterOf(target.getOnPos()));
            lightningBolt.setVisualOnly(true);
            self.level().addFreshEntity(lightningBolt);

            return true;
        }
        return false;
    }
}