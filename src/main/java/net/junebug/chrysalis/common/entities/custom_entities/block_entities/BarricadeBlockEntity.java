package net.junebug.chrysalis.common.entities.custom_entities.block_entities;

import net.junebug.chrysalis.common.blocks.CBlockStateProperties;
import net.junebug.chrysalis.common.blocks.custom_blocks.BarricadeFullBlock;
import net.junebug.chrysalis.common.entities.registry.CBlockEntities;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.util.helpers.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class BarricadeBlockEntity extends BlockEntity {

    /**
     * The block entity for barricade blocks. See more in net.junebug.chrysalis.common.blocks.custom_blocks.BarricadeFullBlock;
     **/

    private BlockState disguisedBlockState = Blocks.AIR.defaultBlockState();
    private final String disguisedBlockStateTag = "disguised_block_state";

    public BarricadeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CBlockEntities.BARRICADE_BLOCK.get(), blockPos, blockState);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(compoundTag, registries);
        this.setDisguisedBlockState(NbtUtils.readBlockState(this.getLevel() != null ? this.getLevel().holderLookup(Registries.BLOCK) : BuiltInRegistries.BLOCK, compoundTag.getCompound(this.disguisedBlockStateTag)));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(compoundTag, registries);
        compoundTag.put(this.disguisedBlockStateTag, NbtUtils.writeBlockState(this.getDisguisedBlockState()));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setLevel(@NotNull Level level) {
        super.setLevel(level);
        if (level.holderLookup(Registries.BLOCK).get(this.getDisguisedBlockState().getBlock().builtInRegistryHolder().key()).isEmpty()) this.setDisguisedBlockState(Blocks.AIR.defaultBlockState());
    }

    public BlockState getDisguisedBlockState() {
        return this.disguisedBlockState;
    }

    private void setDisguisedBlockState(BlockState blockState) {
        this.disguisedBlockState = blockState;
    }

    public void updateDisguisedBlock(BarricadeBlockEntity barricadeBlockEntity, Player player, BlockState blockState) {

        if (barricadeBlockEntity.getLevel() == null) return;

        barricadeBlockEntity.getLevel().setBlockAndUpdate(barricadeBlockEntity.getBlockPos(), barricadeBlockEntity.getBlockState().setValue(CBlockStateProperties.INVISIBLE, true));
        barricadeBlockEntity.getLevel().sendBlockUpdated(barricadeBlockEntity.getBlockPos(), barricadeBlockEntity.getBlockState(), barricadeBlockEntity.getBlockState(), 3);
        barricadeBlockEntity.getLevel().getLightEngine().checkBlock(barricadeBlockEntity.getBlockPos());

        barricadeBlockEntity.setDisguisedBlockState(blockState);
        barricadeBlockEntity.setChanged();

        ParticleOptions disguiseParticles = barricadeBlockEntity.getLevel().getBlockState(barricadeBlockEntity.getBlockPos()).getBlock() instanceof BarricadeFullBlock barricadeFullBlock ? barricadeFullBlock.disguiseParticles : ParticleTypes.FLAME;
        emitSoundAndParticles(barricadeBlockEntity.getLevel(), player, barricadeBlockEntity.getBlockPos(), CSoundEvents.BARRICADE_DISGUISE.get(), true, GameEvent.BLOCK_CHANGE, disguiseParticles, 0.6D, 5);
    }

    public static void emitSoundAndParticles(Level level, Player player, BlockPos blockPos, SoundEvent soundEvent, boolean randomizeSoundPitch, Holder.Reference<GameEvent> gameEvent, ParticleOptions particleOptions, double particleRadius, int particleAmount) {
        if (level == null) return;
        level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, randomizeSoundPitch ? level.getRandom().nextFloat() * 0.4F + 0.8F : 1.0F);
        level.gameEvent(player, gameEvent, blockPos);
        if (particleOptions != null) ParticleHelper.emitParticlesAroundBlock(level, blockPos, particleOptions, 0.0D, particleRadius, particleAmount);
    }
}