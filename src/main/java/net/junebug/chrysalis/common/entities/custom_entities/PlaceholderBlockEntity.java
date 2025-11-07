package net.junebug.chrysalis.common.entities.custom_entities;

import net.junebug.chrysalis.common.entities.registry.CBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlaceholderBlockEntity extends BlockEntity {

    /**
     * The block entity for placeholder blocks. See more in net.junebug.chrysalis.common.blocks.custom_blocks.PlaceholderBlock;
     **/

    public PlaceholderBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CBlockEntities.PLACEHOLDER_BLOCK.get(), blockPos, blockState);
    }
}