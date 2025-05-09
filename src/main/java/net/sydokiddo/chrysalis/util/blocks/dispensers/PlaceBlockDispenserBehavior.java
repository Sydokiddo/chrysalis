package net.sydokiddo.chrysalis.util.blocks.dispensers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.helpers.BlockHelper;
import net.sydokiddo.chrysalis.util.helpers.DebugHelper;
import org.jetbrains.annotations.NotNull;

public class PlaceBlockDispenserBehavior implements DispenseItemBehavior {

    /**
     * Allows for dispensers to place any block in front of them.
     **/

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, @NotNull ItemStack itemStack) {

        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        BlockPos blockPos = blockSource.pos().relative(direction);
        ServerLevel serverLevel = blockSource.level();

        if (BlockHelper.isFree(serverLevel.getBlockState(blockPos)) && !serverLevel.isOutsideBuildHeight(blockPos) && itemStack.getItem() instanceof BlockItem blockItem) {

            BlockState blockState = blockItem.getBlock().defaultBlockState();

            BlockHelper.playDispenserSound(blockSource);
            BlockHelper.playDispenserAnimation(blockSource, direction);

            serverLevel.setBlockAndUpdate(blockPos, blockState);
            serverLevel.playSound(null, blockPos, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, blockState.getSoundType().getVolume(), blockState.getSoundType().getPitch());
            serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);

            DebugHelper.sendDispenserMessage(Chrysalis.LOGGER, Chrysalis.IS_DEBUG, blockState.getBlock().getName().getString(), blockPos);
            itemStack.shrink(1);
            return itemStack;
        }

        Chrysalis.LOGGER.error("Error trying to place {} at {}", itemStack.getDisplayName().getString(), blockPos, new Exception());
        return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}