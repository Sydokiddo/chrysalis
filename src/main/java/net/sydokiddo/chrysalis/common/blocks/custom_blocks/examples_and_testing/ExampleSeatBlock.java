package net.sydokiddo.chrysalis.common.blocks.custom_blocks.examples_and_testing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.chrysalis.common.blocks.CBlockStateProperties;
import net.sydokiddo.chrysalis.common.blocks.custom_blocks.interfaces.SittableBlockInterface;
import org.jetbrains.annotations.NotNull;

public class ExampleSeatBlock extends Block implements SittableBlockInterface {

    /**
     * An example class to show how to integrate the ability to sit on a block.
     **/

    public ExampleSeatBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(CBlockStateProperties.SITTABLE, Boolean.TRUE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CBlockStateProperties.SITTABLE);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {

        if (SittableBlockInterface.isSittable(blockState) && !SittableBlockInterface.isSeatOccupied(level, blockPos)) {
            SittableBlockInterface.startSitting(level, blockPos, player, 0.5D);
            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}