package net.junebug.chrysalis.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
public class ParticleHelper {

    /**
     * Common particle effect methods used by a variety of sources.
     **/

    public static void emitBlockParticles(ServerLevel serverLevel, BlockState blockState, double x, double y, double z, int amount, double speed) {
        if (blockState.getRenderShape() == RenderShape.INVISIBLE) return;
        serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState), x, y, z, amount, 0.0D, 0.0D, 0.0D, speed);
    }

    public static void emitBlockParticlesAtHitPosition(ServerLevel serverLevel, BlockState blockState, BlockHitResult blockHitResult, double yOffset, int amount, double speed) {

        Direction direction = blockHitResult.getDirection();
        Vec3 hitResultLocation = blockHitResult.getLocation().relative(direction, 0.2D);

        emitBlockParticles(
            serverLevel,
            blockState,
            hitResultLocation.x() - (double) (direction == Direction.WEST ? 1.0E-6F : 0.0F),
            hitResultLocation.y() + yOffset,
            hitResultLocation.z() - (double) (direction == Direction.NORTH ? 1.0E-6F : 0.0F),
            amount,
            speed
        );
    }

    public static void emitEntityDigParticles(ServerLevel serverLevel, LivingEntity livingEntity, BlockState blockState, double yOffset, int amount) {
        emitBlockParticles(
            serverLevel,
            blockState,
            livingEntity.getRandomX(1.0D),
            livingEntity.getY() + yOffset,
            livingEntity.getRandomZ(1.0D),
            amount,
            1.0D
        );
    }

    public static void emitItemBreakParticles(ServerLevel serverLevel, ItemStack itemStack, double x, double y, double z, int amount) {
        double randomX = (serverLevel.getRandom().nextFloat() - 0.5D) * 0.08D;
        double randomY = (serverLevel.getRandom().nextFloat() - 0.5D) * 0.08D;
        double randomZ = (serverLevel.getRandom().nextFloat() - 0.5D) * 0.08D;
        serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), x, y, z, amount, randomX, randomY, randomZ, 0.15F);
    }

    public static void emitSmallSmokeParticles(ServerLevel serverLevel, double x, double y, double z, int amount) {
        serverLevel.sendParticles(ParticleTypes.SMOKE, x, y, z, amount, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    public static void emitLargeSmokeParticles(ServerLevel serverLevel, double x, double y, double z, int amount) {
        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, amount, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    public static void emitRedstoneParticlesAroundBlock(Level level, BlockPos blockPos, double yHeight) {

        if (level instanceof ServerLevel serverLevel) {

            for (Direction direction : Direction.values()) {

                if (!level.getBlockState(blockPos.relative(direction)).isSolidRender()) {

                    double x = direction.getAxis() == Direction.Axis.X ? 0.5D + (0.5D * (double) direction.getStepX()) : (double) level.getRandom().nextFloat();
                    double y = direction.getAxis() == Direction.Axis.Y ? 0.5D + (0.5D * (double) direction.getStepY()) : (double) level.getRandom().nextFloat();
                    double z = direction.getAxis() == Direction.Axis.Z ? 0.5D + (0.5D * (double) direction.getStepZ()) : (double) level.getRandom().nextFloat();

                    serverLevel.sendParticles(DustParticleOptions.REDSTONE, (double) blockPos.getX() + x, ((double) blockPos.getY() + y) + yHeight, (double) blockPos.getZ() + z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}