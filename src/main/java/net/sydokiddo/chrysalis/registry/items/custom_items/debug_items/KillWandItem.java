package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.base_classes.ExtraReachDebugUtilityItem;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisDamageSources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class KillWandItem extends ExtraReachDebugUtilityItem {

    public KillWandItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the kill wand item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, Item.@NotNull TooltipContext tooltipContext, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addAttackTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Any mob that is hurt with this item will automatically deal the max integer amount of damage to it.
     **/

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity target, LivingEntity self) {

        if (self.level() instanceof ServerLevel serverLevel && (self instanceof Player player && player.isCreative() || !target.isInvulnerable())) {

            target.hurtServer(serverLevel, target.damageSources().source(ChrysalisDamageSources.KILL_WAND, self), Float.MAX_VALUE);

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(serverLevel, EntitySpawnReason.TRIGGERED);
            assert lightningBolt != null;

            lightningBolt.moveTo(Vec3.atBottomCenterOf(target.getOnPos()));
            lightningBolt.setVisualOnly(true);
            self.level().addFreshEntity(lightningBolt);

            return true;
        }

        return super.hurtEnemy(itemStack, target, self);
    }

    @Nullable @Override
    public DamageSource getDamageSource(LivingEntity livingEntity) {
        return livingEntity.damageSources().source(ChrysalisDamageSources.KILL_WAND);
    }

    @Override
    public boolean shouldDisplayCrosshair(Player player) {
        return super.shouldDisplayCrosshair(player) && player.getMainHandItem().getItem() == this;
    }

    @Override
    public ResourceLocation getCrosshairTextureLocation() {
        return ChrysalisMod.id("hud/kill_wand_crosshair");
    }
}