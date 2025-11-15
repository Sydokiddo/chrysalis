package net.junebug.chrysalis.common.blocks.custom_blocks;

import com.mojang.serialization.MapCodec;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.SimpleFluidloggedBlock;
import net.junebug.chrysalis.common.entities.custom_entities.PlaceholderBlockEntity;
import net.junebug.chrysalis.common.entities.registry.CBlockEntities;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.util.helpers.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class PlaceholderBlock extends BaseEntityBlock implements SimpleFluidloggedBlock {

    /**
     * The class for placeholder blocks, a block that replaces itself with a given block id once loaded, powered, or walked on.
     **/

    // region Initialization and Ticking

    public PlaceholderBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE, PlaceholderBlockModelState.FULL_BLOCK).setValue(CBlockStateProperties.PLACEHOLDER_UPDATE_WHEN_STATE, PlaceholderUpdateWhenState.POWERED).setValue(BlockStateProperties.POWERED, false).setValue(CBlockStateProperties.FLUIDLOGGED, FluidloggedState.AIR));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(PlaceholderBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new PlaceholderBlockEntity(blockPos, blockState);
    }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, CBlockEntities.PLACEHOLDER_BLOCK.get(), level.isClientSide() ? PlaceholderBlockEntity::clientTick : PlaceholderBlockEntity::serverTick);
    }

    // endregion

    // region Block States

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE, CBlockStateProperties.PLACEHOLDER_UPDATE_WHEN_STATE, BlockStateProperties.POWERED, CBlockStateProperties.FLUIDLOGGED);
    }

    @Nullable @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return SimpleFluidloggedBlock.getStateForPlacement(blockPlaceContext, this.defaultBlockState());
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        if (blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlockModelState.FULL_BLOCK) return super.getShape(blockState, blockGetter, blockPos, collisionContext);
        return Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        if (blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlockModelState.BILLBOARD) return RenderShape.INVISIBLE;
        return super.getRenderShape(blockState);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        if (blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlockModelState.FULL_BLOCK) return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
        return Shapes.empty();
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockPos oldPos, @NotNull BlockState oldState, @NotNull RandomSource randomSource) {
        return SimpleFluidloggedBlock.updateShape(blockState, levelReader, scheduledTickAccess, blockPos);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState blockState) {
        return SimpleFluidloggedBlock.getFluidState(blockState, Fluids.EMPTY.defaultFluidState());
    }

    @Override
    public int getLightEmission(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return SimpleFluidloggedBlock.getLightEmission(blockState);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState blockState) {
        if (blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlockModelState.FULL_BLOCK) return super.propagatesSkylightDown(blockState);
        return blockState.getValue(CBlockStateProperties.FLUIDLOGGED) == FluidloggedState.AIR;
    }

    // endregion

    // region Interactions

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {

        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if (player.canUseGameMasterBlocks() && !player.isCrouching() && blockEntity instanceof PlaceholderBlockEntity placeholderBlockEntity && itemStack.getItem() instanceof BlockItem blockItem) {

            BlockState blockToUpdate = blockItem.getBlock().defaultBlockState();
            itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).apply(blockToUpdate);
            placeholderBlockEntity.setBlockStateToUpdate(blockToUpdate);

            level.playSound(null, placeholderBlockEntity.getBlockPos(), CSoundEvents.GENERIC_SPAWNER_CHANGE_ENTITY.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, placeholderBlockEntity.getBlockPos());
            ParticleHelper.emitParticlesAroundBlock(placeholderBlockEntity.getLevel(), placeholderBlockEntity.getBlockPos(), ParticleTypes.HAPPY_VILLAGER, 0.0D, 0.6D, 5);
            player.awardStat(Stats.ITEM_USED.get(blockItem));

            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    protected void neighborChanged(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Block block, @Nullable Orientation orientation, boolean moved) {

        if (level.isClientSide() || blockState.getValue(CBlockStateProperties.PLACEHOLDER_UPDATE_WHEN_STATE) != PlaceholderUpdateWhenState.POWERED) return;

        if (blockState.getValue(BlockStateProperties.POWERED) != level.hasNeighborSignal(blockPos)) {
            level.setBlockAndUpdate(blockPos, blockState.cycle(BlockStateProperties.POWERED));
            ParticleHelper.emitRedstoneParticlesAroundBlock(level, blockPos, 0.0D);
        }
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull Entity entity) {
        this.updateWhenSteppedOn(level, blockPos, blockState, entity, blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) == PlaceholderBlockModelState.FULL_BLOCK);
    }

    @Override
    protected void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Entity entity) {
        this.updateWhenSteppedOn(level, blockPos, blockState, entity, blockState.getValue(CBlockStateProperties.PLACEHOLDER_BLOCK_MODEL_STATE) != PlaceholderBlockModelState.FULL_BLOCK);
        Block.pushEntitiesUp(blockState, level.getBlockEntity(blockPos) instanceof PlaceholderBlockEntity placeholderBlockEntity && !placeholderBlockEntity.getBlockStateToUpdate().isEmpty() ? placeholderBlockEntity.getBlockStateToUpdate() : blockState, level, blockPos);
    }

    private void updateWhenSteppedOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity, boolean canUpdate) {
        if (level.getBlockEntity(blockPos) instanceof PlaceholderBlockEntity placeholderBlockEntity && canUpdate && blockState.getValue(CBlockStateProperties.PLACEHOLDER_UPDATE_WHEN_STATE) == PlaceholderUpdateWhenState.WALKED_ON && entity instanceof Player) PlaceholderBlockEntity.updateBlock(placeholderBlockEntity);
    }

    // endregion

    public enum PlaceholderBlockModelState implements StringRepresentable {

        FULL_BLOCK("full_block"),
        CROSS("cross"),
        BILLBOARD("billboard");

        private final String stateName;

        PlaceholderBlockModelState(String stateName) {
            this.stateName = stateName;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.stateName;
        }
    }

    public enum PlaceholderUpdateWhenState implements StringRepresentable {

        LOADED("loaded"),
        POWERED("powered"),
        WALKED_ON("walked_on");

        private final String stateName;

        PlaceholderUpdateWhenState(String stateName) {
            this.stateName = stateName;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.stateName;
        }
    }
}