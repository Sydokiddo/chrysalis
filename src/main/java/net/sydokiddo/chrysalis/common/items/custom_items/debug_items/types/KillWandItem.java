package net.sydokiddo.chrysalis.common.items.custom_items.debug_items.types;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.misc.CTags;
import net.sydokiddo.chrysalis.util.helpers.EntityHelper;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.shared_classes.ExtraReachDebugUtilityItem;
import net.sydokiddo.chrysalis.common.misc.CDamageTypes;
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
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addAttackTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Any mob that is hurt with this item will automatically deal the max integer amount of damage to it.
     **/

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity target, LivingEntity self) {

        if (self.level() instanceof ServerLevel serverLevel && self instanceof Player player && canAttack(target, player)) {

            target.hurtServer(serverLevel, target.damageSources().source(CDamageTypes.KILL_WAND, player), Float.MAX_VALUE);

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(serverLevel, EntitySpawnReason.TRIGGERED);
            assert lightningBolt != null;

            lightningBolt.moveTo(Vec3.atBottomCenterOf(target.getOnPos()));
            lightningBolt.setVisualOnly(true);
            serverLevel.addFreshEntity(lightningBolt);

            return true;
        }

        return super.hurtEnemy(itemStack, target, self);
    }

    @Nullable @Override
    public DamageSource getDamageSource(LivingEntity livingEntity) {
        return livingEntity.damageSources().source(CDamageTypes.KILL_WAND, livingEntity);
    }

    public static boolean canAttack(Entity target, Player player) {
        return target.isAttackable() && !EntityHelper.targetIsLinkedAllay(player, target) && (player.isCreative() || !target.isInvulnerable());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldDisplayCrosshair(Player player) {
        Entity crosshairEntity = Minecraft.getInstance().crosshairPickEntity;
        if (crosshairEntity != null && !canAttack(crosshairEntity, player)) return false;
        return (super.shouldDisplayCrosshair(player) || crosshairEntity != null && crosshairEntity.getType().is(CTags.DISPLAYS_KILL_WAND_CROSSHAIR)) && player.getMainHandItem().getItem() == this;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getCrosshairTextureLocation() {
        return Chrysalis.resourceLocationId("hud/crosshair/kill_wand");
    }
}