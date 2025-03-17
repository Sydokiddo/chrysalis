package net.sydokiddo.chrysalis.util.blocks.dispensers;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.helpers.BlockHelper;
import net.sydokiddo.chrysalis.util.helpers.DebugHelper;
import org.jetbrains.annotations.NotNull;

public class ShootProjectileDispenserBehavior extends DefaultDispenseItemBehavior {

    /**
     * Allows for dispensers to shoot any custom projectile.
     **/

    private final EntityType<?> entityType;
    private final SoundEvent shootingSound;

    public ShootProjectileDispenserBehavior(EntityType<?> entityType, SoundEvent shootSound) {
        this.entityType = entityType;
        this.shootingSound = shootSound;
    }

    @Override
    public @NotNull ItemStack execute(BlockSource blockSource, @NotNull ItemStack itemStack) {

        Level level = blockSource.level();
        Position position = DispenserBlock.getDispensePosition(blockSource);
        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);

        if (this.entityType == null) return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
        Entity entity = this.entityType.create(level, EntitySpawnReason.DISPENSER);
        assert entity != null;

        if (entity instanceof ThrowableItemProjectile throwableItemProjectile) throwableItemProjectile.setItem(itemStack);
        if (entity instanceof Projectile projectile) projectile.shoot(direction.getStepX(), direction.getStepY() + 0.1D, direction.getStepZ(), 1.1F, 6.0F);
        entity.setPos(position.x(), position.y(), position.z());

        level.addFreshEntity(entity);
        DebugHelper.sendDispenserMessage(Chrysalis.LOGGER, Chrysalis.IS_DEBUG, entity.getName().getString(), blockSource.pos());
        itemStack.shrink(1);
        return itemStack;
    }

    @Override
    protected void playSound(@NotNull BlockSource blockSource) {
        if (this.shootingSound != null) blockSource.level().playSound(null, blockSource.pos(), this.shootingSound, SoundSource.BLOCKS, 1.0F, 1.0F);
        else blockSource.level().levelEvent(1002, blockSource.pos(), 0);
    }
}