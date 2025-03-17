package net.sydokiddo.chrysalis.common.items.custom_items.examples_and_testing;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.common.items.custom_items.CustomBowItem;
import org.jetbrains.annotations.NotNull;

public class ExampleBowItem extends CustomBowItem {

    public ExampleBowItem(Properties properties) {
        super(
            itemStack -> itemStack.is(Items.SNOWBALL),
            Items.SNOWBALL.getDefaultInstance(),
            10,
            SoundEvents.CROSSBOW_LOADING_START.value(),
            SoundEvents.CROSSBOW_LOADING_END.value(),
            null,
            SoundEvents.CROSSBOW_SHOOT, properties
        );
    }

    @Override
    public Projectile getProjectile(@NotNull Level level, @NotNull LivingEntity shooter, @NotNull ItemStack weapon, @NotNull ItemStack ammo, boolean isCrit) {
        return new Snowball(level, shooter, ammo);
    }

    @Override
    public void onShoot(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int useItemTicks) {
        livingEntity.knockback(0.5D, -Mth.sin(livingEntity.getYRot() * (float) (Math.PI / 180.0D)), Mth.cos(livingEntity.getYRot() * (float) (Math.PI / 180.0D)));
        if (livingEntity instanceof ServerPlayer serverPlayer) serverPlayer.getCooldowns().addCooldown(itemStack, 20);
    }
}