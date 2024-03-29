package net.sydokiddo.chrysalis.misc.util.dispenser;

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
import net.sydokiddo.chrysalis.misc.util.helpers.BlockHelper;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class PlaceBlockDispenserBehavior implements DispenseItemBehavior {

    /**
     * Allows for Dispensers to place any block in front of them.
     **/

    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {

        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        BlockPos blockPos = blockSource.pos().relative(direction);
        ServerLevel serverLevel = blockSource.level();

        if (BlockHelper.isBlockStateFree(serverLevel.getBlockState(blockPos)) && !serverLevel.isOutsideBuildHeight(blockPos) && itemStack.getItem() instanceof BlockItem blockItem) {

            BlockState blockState = blockItem.getBlock().defaultBlockState();

            BlockHelper.playDispenserSound(blockSource);
            BlockHelper.playDispenserAnimation(blockSource, direction);

            serverLevel.setBlock(blockPos, blockState, 3);
            serverLevel.playSound(null, blockPos, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, blockState.getSoundType().getVolume(), blockState.getSoundType().getPitch());
            serverLevel.gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);

            if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("{} has been successfully dispensed at {}", blockState.getBlock().getName().getString(), blockPos);

            itemStack.shrink(1);
            return itemStack;
        }

        LOGGER.error("Error trying to place {} at {}", itemStack.getDisplayName().getString(), blockPos, new Exception());
        return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}