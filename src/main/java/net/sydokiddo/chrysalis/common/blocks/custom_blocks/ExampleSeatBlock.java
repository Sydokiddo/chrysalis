package net.sydokiddo.chrysalis.common.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces.SittableBlock;
import org.jetbrains.annotations.NotNull;

public class ExampleSeatBlock extends Block implements SittableBlock {

    /**
     * An example class to show how to integrate the ability to sit on a block.
     **/

    public ExampleSeatBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {

        if (SittableBlock.isSittable() && !SittableBlock.isSeatOccupied(level, blockPos)) {
            SittableBlock.startSitting(level, blockPos, player, 0.5D);
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}