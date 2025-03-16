package net.sydokiddo.chrysalis.common.items.custom_items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class CustomBowItem extends BowItem {

    /**
     * A class for creating a custom bow type, with properties for unique sounds and ammo.
     **/

    private final Predicate<ItemStack> ammo;
    private final ItemStack fallBackAmmo;
    private final int minUseDuration;

    private final SoundEvent
        loadingStartSound,
        loadingEndSound,
        loadingPoweredSound,
        shootingSound
    ;

    public CustomBowItem(Predicate<ItemStack> ammo, ItemStack fallBackAmmo, int minUseDuration, SoundEvent loadingStartSound, SoundEvent loadingEndSound, SoundEvent loadingPoweredSound, SoundEvent shootingSound, Properties properties) {
        super(properties);
        this.ammo = ammo;
        this.fallBackAmmo = fallBackAmmo;
        this.minUseDuration = minUseDuration;
        this.loadingStartSound = loadingStartSound;
        this.loadingEndSound = loadingEndSound;
        this.loadingPoweredSound = loadingPoweredSound;
        this.shootingSound = shootingSound;
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return this.ammo;
    }

    @Override
    public @NotNull ItemStack getDefaultCreativeAmmo(@Nullable Player player, @NotNull ItemStack projectileWeaponItem) {
        return this.fallBackAmmo;
    }

    public Projectile getProjectile(@NotNull Level level, @NotNull LivingEntity shooter, @NotNull ItemStack weapon, @NotNull ItemStack ammo, boolean isCrit) {
        ArrowItem arrowitem = ammo.getItem() instanceof ArrowItem getArrowItem ? getArrowItem : (ArrowItem) Items.ARROW;
        AbstractArrow abstractarrow = arrowitem.createArrow(level, ammo, shooter, weapon);
        if (isCrit) abstractarrow.setCritArrow(true);
        return customArrow(abstractarrow, ammo, weapon);
    }

    @Override
    protected @NotNull Projectile createProjectile(@NotNull Level level, @NotNull LivingEntity shooter, @NotNull ItemStack weapon, @NotNull ItemStack ammo, boolean isCrit) {
        if (this.getProjectile(level, shooter, weapon, ammo, isCrit) instanceof AbstractArrow) return super.createProjectile(level, shooter, weapon, ammo, isCrit);
        return this.getProjectile(level, shooter, weapon, ammo, isCrit);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, int useItemTicks) {

        float bowPull = (itemStack.getUseDuration(livingEntity) - livingEntity.getUseItemRemainingTicks()) / 20.0F;
        final float maxPull = 0.9F;

        if (bowPull == 0.0F && this.loadingStartSound != null) this.playLoadingSound(level, livingEntity, this.loadingStartSound);
        if (bowPull == 0.45F && this.loadingEndSound != null) this.playLoadingSound(level, livingEntity, this.loadingEndSound);
        if (bowPull == maxPull && ItemHelper.getEnchantmentLevel(itemStack, Enchantments.POWER) > 0 && this.loadingPoweredSound != null) this.playLoadingSound(level, livingEntity, this.loadingPoweredSound);

        if (Chrysalis.IS_DEBUG && !level.isClientSide() && bowPull <= maxPull) Chrysalis.LOGGER.info("{} Pull: {}", itemStack.getDisplayName().getString(), bowPull);

        super.onUseTick(level, livingEntity, itemStack, useItemTicks);
    }

    private void playLoadingSound(Level level, LivingEntity livingEntity, SoundEvent soundEvent) {
        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, SoundSource.NEUTRAL);
    }

    @Override
    public boolean releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int useItemTicks) {

        if (!(livingEntity instanceof Player player)) return false;
        ItemStack projectile = player.getProjectile(itemStack);

        if (projectile.isEmpty()) {
            return false;
        } else {

            int useDuration = this.getUseDuration(itemStack, livingEntity) - useItemTicks;
            useDuration = EventHooks.onArrowLoose(itemStack, level, player, useDuration, !projectile.isEmpty());

            if (useDuration < this.minUseDuration) {
                return false;
            } else {

                float powerForTime = getPowerForTime(useDuration);

                if ((double) powerForTime < 0.1D) {
                    return false;
                } else {

                    List<ItemStack> list = draw(itemStack, projectile, player);
                    if (level instanceof ServerLevel serverlevel && !list.isEmpty()) this.shoot(serverlevel, player, player.getUsedItemHand(), itemStack, list, powerForTime * 3.0F, 1.0F, powerForTime == 1.0F, null);
                    this.onShoot(itemStack, level, livingEntity, useItemTicks);

                    if (this.shootingSound != null) level.playSound(null, player.getX(), player.getY(), player.getZ(), this.shootingSound, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + powerForTime * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return true;
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public void onShoot(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int useItemTicks) {}
}