package net.sydokiddo.chrysalis.misc.util.dispenser;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ShootProjectileDispenserBehavior extends DefaultDispenseItemBehavior {

    private final EntityType<?> entityType;
    public final SoundEvent shootSound;

    public ShootProjectileDispenserBehavior(EntityType<?> entityType, SoundEvent shootSound) {
        this.entityType = entityType;
        this.shootSound = shootSound;
    }

    @Override
    public @NotNull ItemStack execute(BlockSource blockSource, ItemStack itemStack) {

        Level level = blockSource.level();
        Position position = DispenserBlock.getDispensePosition(blockSource);
        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);

        Entity entity = this.entityType.create(level);
        assert entity != null;

        if (entity instanceof ThrowableItemProjectile throwableItemProjectile) throwableItemProjectile.setItem(itemStack);
        if (entity instanceof Projectile projectile) projectile.shoot(direction.getStepX(), (float) direction.getStepY() + 0.1F, direction.getStepZ(), 1.1F, 6.0F);
        entity.setPos(position.x(), position.y(), position.z());

        level.addFreshEntity(entity);
        itemStack.shrink(1);
        return itemStack;
    }

    @Override
    protected void playSound(BlockSource blockSource) {
        if (this.shootSound != null) blockSource.level().playSound(null, blockSource.pos(), this.shootSound, SoundSource.BLOCKS, 1.0F, 1.0F);
        else blockSource.level().levelEvent(1002, blockSource.pos(), 0);
    }
}