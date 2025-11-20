package net.junebug.chrysalis.common.blocks.custom_blocks;

import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.Barricade;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.SimpleFluidloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class BarricadeBlock extends FluidloggedMultifaceBlock implements Barricade {

    /**
     * A multiface block that is used to block off areas and deflect projectiles off of it.
     **/

    // region Initialization

    public BarricadeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(getDefaultMultifaceState(this.getStateDefinition()).setValue(CBlockStateProperties.EMITS_AMBIENT_SOUNDS, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CBlockStateProperties.EMITS_AMBIENT_SOUNDS));
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockPos adjacentPos, @NotNull BlockState adjacentState, @NotNull RandomSource randomSource) {
        if (blockState.getValue(CBlockStateProperties.FLUIDLOGGED) != FluidloggedState.AIR) SimpleFluidloggedBlock.scheduleShapeTick(blockState, levelReader, scheduledTickAccess, blockPos);
        return blockState;
    }

    @Override
    protected boolean canBeReplaced(@NotNull BlockState blockState, @NotNull BlockPlaceContext blockPlaceContext) {
        return super.canBeReplaced(blockState, blockPlaceContext) && blockPlaceContext.getPlayer() != null && blockPlaceContext.getPlayer().canUseGameMasterBlocks();
    }

    // endregion

    // region Miscellaneous

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        Barricade.playAmbientSound(level, blockPos, blockState, randomSource);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, Item.@NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        Barricade.addTooltip(list, tooltipFlag, false);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    // endregion
}