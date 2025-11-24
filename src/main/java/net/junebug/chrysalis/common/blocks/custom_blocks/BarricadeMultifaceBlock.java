package net.junebug.chrysalis.common.blocks.custom_blocks;

import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.Barricade;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.SimpleFluidloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.util.List;

public class BarricadeMultifaceBlock extends FluidloggedMultifaceBlock implements Barricade {

    /**
     * A multiface block that is used to block off areas and deflect projectiles off of it.
     **/

    // region Initialization

    public int particleColor = Color.LIGHT_GRAY.getRGB();

    public BarricadeMultifaceBlock(int particleColor, Properties properties) {
        super(properties);
        this.registerDefaultState();
        this.particleColor = particleColor;
    }

    private void registerDefaultState() {
        this.registerDefaultState(getDefaultMultifaceState(this.getStateDefinition()).setValue(CBlockStateProperties.INVISIBLE, false).setValue(CBlockStateProperties.EMITS_AMBIENT_SOUNDS, true).setValue(CBlockStateProperties.EMITS_PARTICLES, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CBlockStateProperties.INVISIBLE).add(CBlockStateProperties.EMITS_AMBIENT_SOUNDS).add(CBlockStateProperties.EMITS_PARTICLES));
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        if (blockState.getValue(CBlockStateProperties.INVISIBLE)) return RenderShape.INVISIBLE;
        return super.getRenderShape(blockState);
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
        Barricade.emitAmbientSoundsAndParticles(level, blockPos, blockState, randomSource, this.particleColor, 0.5D);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, Item.@NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        Barricade.addTooltip(list, tooltipFlag, false);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult blockHitResult) {
        return Barricade.toggleVisibility(blockState, level, blockPos, player, 0.0D, 1, super.useWithoutItem(blockState, level, blockPos, player, blockHitResult));
    }

    // endregion
}