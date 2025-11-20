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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.util.List;

public class BarricadeFullBlock extends TransparentBlock implements Barricade, SimpleFluidloggedBlock {

    /**
     * A block that is used to block off areas and deflect projectiles off of it, with the ability to disguise it as any other block.
     **/

    // region Initialization

    public BarricadeFullBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(CBlockStateProperties.EMITS_AMBIENT_SOUNDS, true).setValue(CBlockStateProperties.FLUIDLOGGED, FluidloggedState.AIR));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CBlockStateProperties.EMITS_AMBIENT_SOUNDS).add(CBlockStateProperties.FLUIDLOGGED));
    }

    @Nullable @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return SimpleFluidloggedBlock.getStateForPlacement(blockPlaceContext, this.defaultBlockState());
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState blockState) {
        return SimpleFluidloggedBlock.getFluidState(blockState, super.getFluidState(blockState));
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockPos oldPos, @NotNull BlockState oldState, @NotNull RandomSource randomSource) {
        return SimpleFluidloggedBlock.updateShape(blockState, levelReader, scheduledTickAccess, blockPos);
    }

    @Override
    public int getLightEmission(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return SimpleFluidloggedBlock.getLightEmission(blockState);
    }

    // endregion

    // region Miscellaneous

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        Barricade.playAmbientSound(level, blockPos, blockState, randomSource);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, Item.@NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        Barricade.addTooltip(list, tooltipFlag, true);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    // endregion
}