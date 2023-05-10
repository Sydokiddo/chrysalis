package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class KillWandItem extends Item {

    public KillWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(this.getDisplayName().withStyle(ChatFormatting.GRAY));
    }

    private MutableComponent getDisplayName() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    @SuppressWarnings("ALL")
    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity self) {
        if (!target.isInvulnerable()) {

            target.hurt(target.damageSources().mobAttack(self), Float.MAX_VALUE);

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(self.level());
            lightningBolt.moveTo(Vec3.atBottomCenterOf(target.getOnPos()));
            lightningBolt.setVisualOnly(true);
            self.level().addFreshEntity(lightningBolt);

            return true;
        }
        return false;
    }
}
