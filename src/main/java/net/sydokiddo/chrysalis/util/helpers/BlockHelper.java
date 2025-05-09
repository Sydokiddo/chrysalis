package net.sydokiddo.chrysalis.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.util.function.ToIntFunction;

@SuppressWarnings("unused")
public class BlockHelper {

    /**
     * Methods to get various pieces of information from blocks.
     **/

    public static boolean isFree(BlockState blockState) {
        return blockState.is(BlockTags.REPLACEABLE) || blockState.isEmpty() || blockState.isAir();
    }

    public static ToIntFunction<BlockState> shouldEmitLight(int lightAmount) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightAmount : 0;
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
}