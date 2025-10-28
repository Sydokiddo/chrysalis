package net.junebug.chrysalis.common.blocks.custom_blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class LayerBlock extends FallingBlock implements Fallable {

    /**
     * A class for custom layer blocks similar to snow layers, with options to make it have gravity similar to sand or gravel.
     **/

    // region Initialization

    private final TagKey<Block>
        cannotSurviveOn,
        canSurviveOn
    ;

    private final boolean hasGravity;

    private final int
        fallDelay,
        dustColor
    ;

    public LayerBlock(Properties properties, TagKey<Block> cannotSurviveOn, TagKey<Block> canSurviveOn, int fallDelay, int dustColor) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.LAYERS, 1));
        this.cannotSurviveOn = cannotSurviveOn;
        this.canSurviveOn = canSurviveOn;
        this.hasGravity = true;
        this.fallDelay = fallDelay;
        this.dustColor = dustColor;
    }

    public LayerBlock(Properties properties, TagKey<Block> cannotSurviveOn, TagKey<Block> canSurviveOn) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.LAYERS, 1));
        this.cannotSurviveOn = cannotSurviveOn;
        this.canSurviveOn = canSurviveOn;
        this.hasGravity = false;
        this.fallDelay = 0;
        this.dustColor = 0;
    }

    @Override
    protected @NotNull MapCodec<? extends LayerBlock> codec() {
        return LayerBlock.simpleCodec(properties -> new LayerBlock(properties, this.cannotSurviveOn, this.canSurviveOn, this.fallDelay, this.dustColor));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.LAYERS);
    }

    // endregion

    // region Hitbox

    private static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[] {
        Shapes.empty(),
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
        Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };

    @Override
    protected @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        return SHAPE_BY_LAYER[blockState.getValue(BlockStateProperties.LAYERS)];
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        return SHAPE_BY_LAYER[blockState.getValue(BlockStateProperties.LAYERS) - 1];
    }

    @Override
    protected @NotNull VoxelShape getBlockSupportShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return SHAPE_BY_LAYER[blockState.getValue(BlockStateProperties.LAYERS)];
    }

    @Override
    protected @NotNull VoxelShape getVisualShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext context) {
        return SHAPE_BY_LAYER[blockState.getValue(BlockStateProperties.LAYERS)];
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState blockState) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return blockState.getValue(BlockStateProperties.LAYERS) == 8 ? 0.2F : 1.0F;
    }

    // endregion

    // region Ticking

    @Override
    protected void onPlace(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState oldState, boolean isMoving) {
        if (this.hasGravity) super.onPlace(blockState, level, blockPos, oldState, isMoving);
    }

    @Override
    protected void tick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (this.hasGravity) super.tick(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (this.hasGravity) super.animateTick(blockState, level, blockPos, randomSource);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockState belowState = levelReader.getBlockState(blockPos.below());
        if (belowState.is(this.cannotSurviveOn)) return false;
        else return belowState.is(this.canSurviveOn) || this.hasGravity && belowState.isAir() || Block.isFaceFull(belowState.getCollisionShape(levelReader, blockPos.below()), Direction.UP) || belowState.is(this) && belowState.getValue(BlockStateProperties.LAYERS) == 8;
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockPos oldPos, @NotNull BlockState oldState, @NotNull RandomSource randomSource) {
        return !blockState.canSurvive(levelReader, blockPos) ? Blocks.AIR.defaultBlockState() : this.hasGravity ? super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, oldPos, oldState, randomSource) : blockState;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        int layers = state.getValue(BlockStateProperties.LAYERS);
        if (!useContext.getItemInHand().is(this.asItem()) || layers >= 8) return layers == 1;
        else return !useContext.replacingClickedOnBlock() || useContext.getClickedFace() == Direction.UP;
    }

    @Nullable @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext useContext) {
        BlockState blockState = useContext.getLevel().getBlockState(useContext.getClickedPos());
        if (blockState.is(this)) return blockState.setValue(BlockStateProperties.LAYERS, Math.min(8, blockState.getValue(BlockStateProperties.LAYERS) + 1));
        else return super.getStateForPlacement(useContext);
    }

    // endregion

    // region Miscellaneous

    @Override
    protected int getDelayAfterPlace() {
        return this.fallDelay;
    }

    @Override
    public int getDustColor(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return this.dustColor;
    }

    // endregion
}