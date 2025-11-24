package net.junebug.chrysalis.common.blocks.custom_blocks;

import com.mojang.serialization.MapCodec;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.Barricade;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.FluidloggedState;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.SimpleFluidloggedBlock;
import net.junebug.chrysalis.common.entities.custom_entities.block_entities.BarricadeBlockEntity;
import net.junebug.chrysalis.common.misc.CTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class BarricadeFullBlock extends BaseEntityBlock implements Barricade, SimpleFluidloggedBlock {

    /**
     * A block that is used to block off areas and deflect projectiles off of it, with the ability to disguise it as any other block.
     **/

    // region Initialization

    public int particleColor = Color.LIGHT_GRAY.getRGB();
    public ParticleOptions disguiseParticles = ParticleTypes.FLAME;

    private BarricadeFullBlock(Properties properties) {
        super(properties);
        this.registerDefaultState();
    }

    public BarricadeFullBlock(int particleColor, ParticleOptions disguiseParticles, Properties properties) {
        super(properties);
        this.registerDefaultState();
        this.particleColor = particleColor;
        this.disguiseParticles = disguiseParticles;
    }

    private void registerDefaultState() {
        this.registerDefaultState(this.getStateDefinition().any().setValue(CBlockStateProperties.INVISIBLE, false).setValue(CBlockStateProperties.EMITS_AMBIENT_SOUNDS, true).setValue(CBlockStateProperties.EMITS_PARTICLES, true).setValue(CBlockStateProperties.FLUIDLOGGED, FluidloggedState.AIR));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BarricadeFullBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new BarricadeBlockEntity(blockPos, blockState);
    }

    private Optional<BarricadeBlockEntity> getBarricadeBlockEntity(BlockGetter blockGetter, BlockPos blockPos) {
        if (blockGetter.getBlockEntity(blockPos) instanceof BarricadeBlockEntity barricadeBlockEntity) return Optional.of(barricadeBlockEntity);
        return Optional.empty();
    }

    // endregion

    // region Block States

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(CBlockStateProperties.INVISIBLE).add(CBlockStateProperties.EMITS_AMBIENT_SOUNDS).add(CBlockStateProperties.EMITS_PARTICLES).add(CBlockStateProperties.FLUIDLOGGED));
    }

    @Nullable @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return SimpleFluidloggedBlock.getStateForPlacement(blockPlaceContext, this.defaultBlockState());
    }

    @Override
    protected @NotNull VoxelShape getVisualShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        if (this.getBarricadeBlockEntity(blockGetter, blockPos).isEmpty() || this.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState().isEmpty()) return Shapes.empty();
        return this.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState().getVisualShape(blockGetter, blockPos, collisionContext);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        if (blockState.getValue(CBlockStateProperties.INVISIBLE)) return RenderShape.INVISIBLE;
        return super.getRenderShape(blockState);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockPos oldPos, @NotNull BlockState oldState, @NotNull RandomSource randomSource) {
        return SimpleFluidloggedBlock.updateShape(blockState, levelReader, scheduledTickAccess, blockPos);
    }

    @Override
    protected boolean skipRendering(@NotNull BlockState blockState, BlockState adjacentBlockState, @NotNull Direction direction) {
        return adjacentBlockState.getBlock() instanceof BarricadeFullBlock || super.skipRendering(blockState, adjacentBlockState, direction);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState blockState) {
        return SimpleFluidloggedBlock.getFluidState(blockState, super.getFluidState(blockState));
    }

    @Override
    public int getLightEmission(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        if (this.getBarricadeBlockEntity(blockGetter, blockPos).isEmpty() || this.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState().isEmpty()) return SimpleFluidloggedBlock.getLightEmission(super.getLightEmission(blockState, blockGetter, blockPos), blockState);
        return SimpleFluidloggedBlock.getLightEmission(this.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState().getLightEmission(blockGetter, blockPos), blockState);
    }

    @Override
    protected float getShadeBrightness(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        if (this.getBarricadeBlockEntity(blockGetter, blockPos).isEmpty() || this.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState().isEmpty()) return 1.0F;
        return this.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState().getShadeBrightness(blockGetter, blockPos);
    }

    // endregion

    // region Miscellaneous

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        Barricade.emitAmbientSoundsAndParticles(level, blockPos, blockState, randomSource, this.particleColor, 1.0D);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, Item.@NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        Barricade.addTooltip(list, tooltipFlag, true);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {

        InteractionResult original = super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);

        if (player.canUseGameMasterBlocks() && !player.isCrouching() && level.getBlockEntity(blockPos) instanceof BarricadeBlockEntity barricadeBlockEntity && itemStack.getItem() instanceof BlockItem blockItem && !blockItem.getBlock().defaultBlockState().is(CTags.BARRICADES)) {

            BlockState disguisedBlockState = blockItem.getBlock().defaultBlockState();
            if (disguisedBlockState == barricadeBlockEntity.getDisguisedBlockState()) return original;

            itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY).apply(disguisedBlockState);
            barricadeBlockEntity.updateDisguisedBlock(barricadeBlockEntity, player, disguisedBlockState);
            player.awardStat(Stats.ITEM_USED.get(blockItem));

            return InteractionResult.SUCCESS;
        }

        return original;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult blockHitResult) {
        return Barricade.toggleVisibility(blockState, level, blockPos, player, 0.6D, 5, super.useWithoutItem(blockState, level, blockPos, player, blockHitResult));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected @NotNull ItemStack getCloneItemStack(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean includeData) {
        if (this.getBarricadeBlockEntity(levelReader, blockPos).isEmpty() || this.getBarricadeBlockEntity(levelReader, blockPos).get().getDisguisedBlockState().isEmpty() || !blockState.getValue(CBlockStateProperties.INVISIBLE)) return super.getCloneItemStack(levelReader, blockPos, blockState, includeData);
        return (this.getBarricadeBlockEntity(levelReader, blockPos).get().getDisguisedBlockState().getCloneItemStack(levelReader, blockPos, includeData));
    }

    @Override
    public @NotNull SoundType getSoundType(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @Nullable Entity entity) {
        if (this.getBarricadeBlockEntity(levelReader, blockPos).isEmpty() || this.getBarricadeBlockEntity(levelReader, blockPos).get().getDisguisedBlockState().isEmpty()) return super.getSoundType(blockState, levelReader, blockPos, entity);
        return this.getBarricadeBlockEntity(levelReader, blockPos).get().getDisguisedBlockState().getSoundType(levelReader, blockPos, entity);
    }

    // endregion
}