package net.junebug.chrysalis.common.blocks.custom_blocks.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.entities.registry.CEntities;
import net.junebug.chrysalis.common.entities.custom_entities.Seat;

public interface SittableBlockInterface {

    /**
     * Any block that integrates this interface can be sat on by utilizing the seat entity.
     **/

    static boolean isSittable(BlockState blockState) {
        return blockState.getValueOrElse(CBlockStateProperties.SITTABLE, false);
    }

    static boolean isSeatOccupied(Level level, BlockPos blockPos) {
        return !level.getEntitiesOfClass(Seat.class, new AABB(blockPos)).isEmpty();
    }

    static void startSitting(Level level, BlockPos blockPos, Entity entity, double seatYHeight) {

        if (level.isClientSide()) return;

        Seat seat = CEntities.SEAT.get().create(level, EntitySpawnReason.TRIGGERED);
        assert seat != null;
        seat.setPos(blockPos.getX() + 0.5D, blockPos.getY() + seatYHeight, blockPos.getZ() + 0.5D);

        level.addFreshEntity(seat);
        entity.startRiding(seat);

        level.updateNeighbourForOutputSignal(blockPos, level.getBlockState(blockPos).getBlock());
    }
}