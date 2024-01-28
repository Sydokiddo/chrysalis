package net.sydokiddo.chrysalis.misc.util.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import java.util.function.ToIntFunction;

@SuppressWarnings("unused")
public class BlockHelper {

    public static boolean isBlockStateFree(BlockState blockState) {
        return blockState.is(BlockTags.REPLACEABLE);
    }

    public static ToIntFunction<BlockState> blockStateShouldEmitLight(int lightAmount) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightAmount : 0;
    }

    public static void popResourceBelow(Level level, BlockPos blockPos, ItemStack itemStack, double itemDropOffset) {

        double itemHeight = (double) EntityType.ITEM.getHeight() / 2.0;
        double x = (double) blockPos.getX() + 0.5 + Mth.nextDouble(level.getRandom(), -0.25, 0.25);
        double y = (double) blockPos.getY() - itemDropOffset + Mth.nextDouble(level.getRandom(), -0.25, 0.25) - itemHeight;
        double z = (double) blockPos.getZ() + 0.5 + Mth.nextDouble(level.getRandom(), -0.25, 0.25);

        if (!level.isClientSide() && !itemStack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            ItemEntity itemEntity = new ItemEntity(level, x, y, z, itemStack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
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