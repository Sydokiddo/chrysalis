package net.junebug.chrysalis.common.blocks.custom_blocks.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.entities.registry.CEntities;
import net.junebug.chrysalis.common.entities.custom_entities.Seat;

public interface SittableBlock {

    /**
     * Any block that integrates this interface can be sat on by utilizing the seat entity.
     **/

    static boolean isSittable(BlockState blockState) {
        return blockState.getValueOrElse(CBlockStateProperties.SITTABLE, false);
    }

    static boolean isSeatOccupied(Level level, BlockPos blockPos) {
        return !level.getEntitiesOfClass(Seat.class, new AABB(blockPos)).isEmpty();
    }

    default SoundEvent getSittingSound() {
        return SoundEvents.EMPTY;
    }

    static void startSitting(SittableBlock sittableBlock, Level level, BlockPos blockPos, Entity entity, double seatYHeight) {

        if (level.isClientSide()) return;

        if (sittableBlock.getSittingSound() != null && sittableBlock.getSittingSound() != SoundEvents.EMPTY) {
            level.playSound(null, blockPos, sittableBlock.getSittingSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.ENTITY_MOUNT, blockPos);
        }

        Seat seat = CEntities.SEAT.get().create(level, EntitySpawnReason.TRIGGERED);
        assert seat != null;
        seat.setPos(blockPos.getX() + 0.5D, blockPos.getY() + seatYHeight, blockPos.getZ() + 0.5D);

        level.addFreshEntity(seat);
        entity.startRiding(seat);

        level.updateNeighbourForOutputSignal(blockPos, level.getBlockState(blockPos).getBlock());
    }
}