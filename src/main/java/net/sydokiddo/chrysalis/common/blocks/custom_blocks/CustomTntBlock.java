package net.sydokiddo.chrysalis.common.blocks.custom_blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

public class CustomTntBlock extends TntBlock {

    /**
     * A class for custom tnt blocks, with a customizable sound and the primed tnt entity that spawns when ignited.
     **/

    // region Initialization

    public CustomTntBlock(Properties properties) {
        super(properties);
    }

    public Entity getEntity(Level level, BlockPos blockPos, LivingEntity owner) {
        return new PrimedTnt(level, (double) blockPos.getX() + 0.5D, blockPos.getY(), (double) blockPos.getZ() + 0.5D, owner);
    }

    public SoundEvent getSoundEvent() {
        return SoundEvents.TNT_PRIMED;
    }

    // endregion

    // region Exploding

    @Override
    public void onCaughtFire(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @Nullable Direction direction, @Nullable LivingEntity owner) {
        this.tryExploding(level, blockPos, owner);
    }

    public void tryExploding(Level level, BlockPos blockPos, LivingEntity owner) {
        if (!level.isClientSide()) {
            Entity entity = this.getEntity(level, blockPos, owner);
            this.setExplodeData(entity);
            level.addFreshEntity(entity);
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), this.getSoundEvent(), SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(owner, GameEvent.PRIME_FUSE, blockPos);
        }
    }

    public void setExplodeData(Entity entity) {}

    @Override
    public void wasExploded(@NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull Explosion explosion) {
        Entity entity = this.getEntity(serverLevel, blockPos, explosion.getIndirectSourceEntity());
        this.setWasExplodedData(entity);
        serverLevel.addFreshEntity(entity);
    }

    public void setWasExplodedData(Entity entity) {}

    // endregion
}