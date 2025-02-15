package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.base_classes.ExtraReachDebugUtilityItem;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisDamageSources;
import java.util.List;

public class KillWandItem extends ExtraReachDebugUtilityItem {

    public KillWandItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the kill wand item.
     **/

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ItemHelper.addAttackTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Any mob that is hurt with this item will automatically deal the max integer amount of damage to it.
     **/

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity self) {

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

    @Environment(EnvType.CLIENT)
    @Override
    public boolean shouldDisplayCrosshair(Player player) {
        return super.shouldDisplayCrosshair(player) && player.getMainHandItem().getItem() == this;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ResourceLocation getCrosshairTextureLocation() {
        return Chrysalis.id("hud/kill_wand_crosshair");
    }
}