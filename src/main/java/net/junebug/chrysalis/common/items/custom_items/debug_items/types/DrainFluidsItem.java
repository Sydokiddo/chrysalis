package net.junebug.chrysalis.common.items.custom_items.debug_items.types;

import net.junebug.chrysalis.util.helpers.EntityHelper;
import net.junebug.chrysalis.util.helpers.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes.DebugUtilityItem;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.util.helpers.ItemHelper;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

public class DrainFluidsItem extends DebugUtilityItem {

    public DrainFluidsItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds a custom tooltip to the drain fluids item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Removes nearby fluids in a radius around the player when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {

        int fluidSearchRadius = 8;

        for (BlockPos blockPos : BlockPos.withinManhattan(player.blockPosition(), fluidSearchRadius, fluidSearchRadius, fluidSearchRadius)) {
            if (this.tryDrainingFluids(level, blockPos)) {

                if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
                    EntityHelper.playItemUseNotifySound(serverPlayer, CSoundEvents.DRAIN_FLUIDS_USE.get());
                    ParticleHelper.emitParticlesAroundEntity(serverPlayer, ParticleTypes.FALLING_WATER, 1.0D, 10);
                    serverPlayer.awardStat(Stats.ITEM_USED.get(this));
                    DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("gui.chrysalis.drain_fluids.message"));
                }

                return InteractionResult.SUCCESS.heldItemTransformedTo(player.getItemInHand(interactionHand));
            }
        }

        return super.use(level, player, interactionHand);
    }

    private boolean tryDrainingFluids(Level level, BlockPos blockPos) {
        return BlockPos.breadthFirstTraversal(
            blockPos,
            16,
            Integer.MAX_VALUE,
            (getPos, consumer) -> { for (Direction direction : Direction.values()) consumer.accept(getPos.relative(direction)); },
            fluidPos -> this.updateFluidPos(level, fluidPos)
        ) > 0;
    }

    private BlockPos.TraversalNodeStatus updateFluidPos(Level level, BlockPos fluidPos) {

        BlockState blockState = level.getBlockState(fluidPos);
        if (blockState.getFluidState().isEmpty()) return BlockPos.TraversalNodeStatus.SKIP;

        if (blockState.getBlock() instanceof LiquidBlock) {
            level.setBlockAndUpdate(fluidPos, Blocks.AIR.defaultBlockState());
        } else {

            Optional<FluidloggedState> fluidloggedState = blockState.getOptionalValue(CBlockStateProperties.FLUIDLOGGED);

            if (fluidloggedState.isPresent() && fluidloggedState.get() != FluidloggedState.AIR) {
                level.setBlockAndUpdate(fluidPos, blockState.setValue(CBlockStateProperties.FLUIDLOGGED, FluidloggedState.AIR));
            } else if (blockState.getValueOrElse(BlockStateProperties.WATERLOGGED, false)) {
                level.setBlockAndUpdate(fluidPos, blockState.setValue(BlockStateProperties.WATERLOGGED, false));
            } else if (blockState.is(CTags.INHERENTLY_WATERLOGGED)) {
                Block.dropResources(blockState, level, fluidPos, blockState.hasBlockEntity() ? level.getBlockEntity(fluidPos) : null);
                level.setBlockAndUpdate(fluidPos, Blocks.AIR.defaultBlockState());
            }
        }

        return BlockPos.TraversalNodeStatus.ACCEPT;
    }
}