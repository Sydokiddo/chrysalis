package net.junebug.chrysalis.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import java.util.function.ToIntFunction;

@SuppressWarnings("unused")
public class BlockHelper {

    /**
     * Methods to get various types of information from blocks.
     **/

    public static boolean isFree(Level level, BlockPos blockPos) {
        return level.isInWorldBounds(blockPos) && (level.isEmptyBlock(blockPos) || level.getBlockState(blockPos).canBeReplaced());
    }

    public static ToIntFunction<BlockState> shouldEmitLight(int lightLevel) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightLevel : 0;
    }

    public static void popResourceBelow(ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, double itemDropOffset) {

        double x = (double) blockPos.getX() + 0.5D + Mth.nextDouble(serverLevel.getRandom(), -0.25D, 0.25D);
        double y = (double) blockPos.getY() - itemDropOffset + Mth.nextDouble(serverLevel.getRandom(), -0.25D, 0.25D) - (EntityType.ITEM.getHeight() / 2.0D);
        double z = (double) blockPos.getZ() + 0.5D + Mth.nextDouble(serverLevel.getRandom(), -0.25D, 0.25D);

        if (!serverLevel.isClientSide() && !itemStack.isEmpty() && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            ItemEntity itemEntity = new ItemEntity(serverLevel, x, y, z, itemStack);
            itemEntity.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(itemEntity);
        }
    }

    public static final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

    public static void playDispenserSound(BlockSource blockSource) {
        blockSource.level().levelEvent(1000, blockSource.pos(), 0);
    }

    public static void playDispenserFailSound(BlockSource blockSource) {
        blockSource.level().levelEvent(1001, blockSource.pos(), 0);
    }

    public static void playDispenserShootingSound(BlockSource blockSource) {
        blockSource.level().levelEvent(1002, blockSource.pos(), 0);
    }

    public static void playDispenserAnimation(BlockSource blockSource, Direction direction) {
        blockSource.level().levelEvent(2000, blockSource.pos(), direction.get3DDataValue());
    }

    public static void emitBlockStateChangeEvents(BlockState blockState, Level level, BlockPos blockPos, Property<Boolean> property, SoundEvent toggleOnSound, SoundEvent toggleOffSound) {

        SoundEvent soundEvent;
        Holder.Reference<GameEvent> gameEvent;

        if (blockState.getValue(property)) {
            soundEvent = toggleOffSound;
            gameEvent = GameEvent.BLOCK_DEACTIVATE;
        } else {
            soundEvent = toggleOnSound;
            gameEvent = GameEvent.BLOCK_ACTIVATE;
        }

        level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS);
        level.gameEvent(null, gameEvent, blockPos);
    }

    public static void cycleRedstonePoweredState(BlockState blockState, Level level, BlockPos blockPos, Property<Boolean> property, SoundEvent toggleOnSound, SoundEvent toggleOffSound, double particleYHeight) {
        emitBlockStateChangeEvents(blockState, level, blockPos, property, toggleOnSound, toggleOffSound);
        ParticleHelper.emitRedstoneParticlesAroundBlock(level, blockPos, particleYHeight);
        level.setBlockAndUpdate(blockPos, blockState.cycle(property));
    }
}