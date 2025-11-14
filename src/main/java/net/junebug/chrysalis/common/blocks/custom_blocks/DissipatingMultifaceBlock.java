package net.junebug.chrysalis.common.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.interfaces.DissipatingBlockInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DissipatingMultifaceBlock extends MultifaceBlock implements DissipatingBlockInterface {

    /**
     * A class for blocks that dissipate much like the normal DissipatingBlock class, but for multiface blocks (such as glow lichen, sculk veins, etc.).
     **/

    // region Initialization

    private final int ticksUntilDissipation;
    private final boolean dropBlock;

    private final SoundType
        normalSoundType,
        dissipatedSoundType
    ;

    public DissipatingMultifaceBlock(Properties properties, boolean dissipatesByDefault, int ticksUntilDissipation, boolean dropBlock, SoundType normalSoundType, SoundType dissipatedSoundType) {
        super(properties);
        this.registerDefaultState(getDefaultMultifaceState(this.getStateDefinition()).setValue(CBlockStateProperties.DISSIPATES, dissipatesByDefault).setValue(CBlockStateProperties.DESTROYED, false));
        this.ticksUntilDissipation = ticksUntilDissipation;
        this.dropBlock = dropBlock;
        this.normalSoundType = normalSoundType;
        this.dissipatedSoundType = dissipatedSoundType;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CBlockStateProperties.DISSIPATES, CBlockStateProperties.DESTROYED);
    }

    // endregion

    // region Ticking

    @Override
    protected void onPlace(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(blockState, level, blockPos, oldState, isMoving);
        this.scheduleTick(level, blockPos, this, this.ticksUntilDissipation);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockPos oldPos, @NotNull BlockState oldState, @NotNull RandomSource randomSource) {
        this.scheduleTick(scheduledTickAccess, blockPos, this, this.ticksUntilDissipation);
        return super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, oldPos, oldState, randomSource);
    }

    @Override
    protected void tick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        this.dissipateBlock(serverLevel, blockPos, blockState, this.dropBlock);
    }

    // endregion

    // region Miscellaneous

    @Override
    public @NotNull SoundType getSoundType(BlockState blockState, @NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @Nullable Entity entity) {
        return blockState.getValue(CBlockStateProperties.DESTROYED) && this.dissipatedSoundType != null ? this.dissipatedSoundType : this.normalSoundType;
    }

    // endregion
}