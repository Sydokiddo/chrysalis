package net.junebug.chrysalis.common.entities.custom_entities;

import net.junebug.chrysalis.common.entities.registry.CBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlaceholderBlockEntity extends BlockEntity {

    public PlaceholderBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CBlockEntities.PLACEHOLDER.get(), blockPos, blockState);
    }
}