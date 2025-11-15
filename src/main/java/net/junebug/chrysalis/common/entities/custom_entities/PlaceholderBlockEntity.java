package net.junebug.chrysalis.common.entities.custom_entities;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.PlaceholderBlock;
import net.junebug.chrysalis.common.entities.registry.CBlockEntities;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.util.helpers.ParticleHelper;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class PlaceholderBlockEntity extends BlockEntity {

    /**
     * The block entity for placeholder blocks. See more in net.junebug.chrysalis.common.blocks.custom_blocks.PlaceholderBlock;
     **/

    public int animationTime;
    private BlockState blockState;
    private final String cachedBlockStateTag = "cached_block_state";

    public PlaceholderBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CBlockEntities.PLACEHOLDER_BLOCK.get(), blockPos, blockState);
    }

    @SuppressWarnings("unused")
    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, PlaceholderBlockEntity placeholderBlockEntity) {
        ++placeholderBlockEntity.animationTime;
    }

    @SuppressWarnings("unused")
    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, PlaceholderBlockEntity placeholderBlockEntity) {
        if (blockState.getValue(CBlockStateProperties.PLACEHOLDER_UPDATE_WHEN_STATE) == PlaceholderBlock.PlaceholderUpdateWhenState.POWERED && blockState.getValue(BlockStateProperties.POWERED)) PlaceholderBlockEntity.updateBlock(placeholderBlockEntity);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.getBlockState().getValue(CBlockStateProperties.PLACEHOLDER_UPDATE_WHEN_STATE) == PlaceholderBlock.PlaceholderUpdateWhenState.LOADED) PlaceholderBlockEntity.updateBlock(this);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider registries) {

        super.loadAdditional(compoundTag, registries);

        if (compoundTag.contains(this.cachedBlockStateTag)) {
            BlockState parsedState = this.parseBlockState(compoundTag, registries);
            this.setBlockStateToUpdate(parsedState);
            if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Loaded cached block state from placeholder block: {}", parsedState);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider registries) {

        super.saveAdditional(compoundTag, registries);

        compoundTag.putString(this.cachedBlockStateTag, this.getBlockStateToUpdate().toString());
        if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Saving cached block state to placeholder block: {}", this.getBlockStateToUpdate().toString());
    }

    public BlockState getBlockStateToUpdate() {
        return this.blockState == null || this.blockState.isEmpty() ? Blocks.AIR.defaultBlockState() : this.blockState;
    }

    public void setBlockStateToUpdate(BlockState blockState) {
        this.blockState = blockState;
        if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Set placeholder block cached block state: {}", blockState);
    }

    public BlockState parseBlockState(CompoundTag compoundTag, HolderLookup.@NotNull Provider registries) {

        String tag = compoundTag.getString(this.cachedBlockStateTag);
        BlockState blockState;

        try {
            blockState = BlockStateParser.parseForBlock(registries.lookupOrThrow(Registries.BLOCK), tag, true).blockState();
            if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Parsed placeholder block cached block state: {}", blockState);
        } catch (CommandSyntaxException exception) {
            if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.error("Failed to parse placeholder block cached block state: {}", tag.isEmpty() ? "?" : tag);
            throw new RuntimeException(exception);
        }

        return blockState;
    }

    public static void updateBlock(PlaceholderBlockEntity placeholderBlockEntity) {

        BlockState blockState = placeholderBlockEntity.getBlockStateToUpdate();
        if (placeholderBlockEntity.getLevel() == null || blockState.isEmpty()) return;

        placeholderBlockEntity.setChanged();
        placeholderBlockEntity.getLevel().setBlockAndUpdate(placeholderBlockEntity.getBlockPos(), blockState);
        placeholderBlockEntity.getLevel().playSound(null, placeholderBlockEntity.getBlockPos(), CSoundEvents.PLACEHOLDER_BLOCK_UPDATE.get(), SoundSource.BLOCKS, 1.0F, placeholderBlockEntity.getLevel().getRandom().nextFloat() * 0.4F + 0.8F);
        placeholderBlockEntity.getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, placeholderBlockEntity.getBlockPos());
        ParticleHelper.emitParticlesAroundBlock(placeholderBlockEntity.getLevel(), placeholderBlockEntity.getBlockPos(), ParticleTypes.FLAME, 0.0D, 0.6D, 5);
    }
}