package net.sydokiddo.chrysalis.registry.entities.custom_entities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.registry.blocks.custom_blocks.interfaces.SittableBlock;
import org.jetbrains.annotations.NotNull;

public class Seat extends Entity {

    /**
     * The seat entity that sittable blocks utilize.
     **/

    public Seat(EntityType<? extends Seat> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {}

    @Override
    public void tick() {

        if (this.level().isClientSide()) return;

        BlockState blockState = this.level().getBlockState(this.blockPosition());
        boolean canSit = blockState.getBlock() instanceof SittableBlock && SittableBlock.isSittable();
        if (this.isVehicle() && canSit) return;

        this.discard();
        this.level().updateNeighbourForOutputSignal(this.blockPosition(), blockState.getBlock());
    }

    @Override
    protected boolean canRide(@NotNull Entity entity) {
        return true;
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity livingEntity) {

        Vec3 safeVec;
        Direction original = this.getDirection();
        Direction[] offsets = {original, original.getClockWise(), original.getCounterClockWise(), original.getOpposite()};

        for (Direction directions : offsets) {
            safeVec = DismountHelper.findSafeDismountLocation(livingEntity.getType(), this.level(), this.blockPosition().relative(directions), false);
            if (safeVec != null) return safeVec.add(0.0D, 0.25D, 0.0D);
        }

        return super.getDismountLocationForPassenger(livingEntity);
    }

    @Override
    protected void addPassenger(@NotNull Entity entity) {
        if (entity instanceof TamableAnimal tamableAnimal) tamableAnimal.setInSittingPose(true);
        super.addPassenger(entity);
    }

    @Override
    protected void removePassenger(@NotNull Entity entity) {
        if (entity instanceof TamableAnimal tamableAnimal) tamableAnimal.setInSittingPose(false);
        super.removePassenger(entity);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float damageAmount) {
        return false;
    }
}