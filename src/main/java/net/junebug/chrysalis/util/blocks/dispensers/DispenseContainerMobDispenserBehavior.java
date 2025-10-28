package net.junebug.chrysalis.util.blocks.dispensers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.util.helpers.BlockHelper;
import net.junebug.chrysalis.common.items.custom_items.MobInContainerItem;
import net.junebug.chrysalis.common.items.custom_items.MobInFluidBucketItem;
import net.junebug.chrysalis.util.helpers.DebugHelper;
import org.jetbrains.annotations.NotNull;

public class DispenseContainerMobDispenserBehavior implements DispenseItemBehavior {

    /**
     * Dispenses any mob container item.
     **/

    public static final DispenseContainerMobDispenserBehavior INSTANCE = new DispenseContainerMobDispenserBehavior();

    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, @NotNull ItemStack itemStack) {

        BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
        ServerLevel level = blockSource.level();

        if (BlockHelper.isFree(level.getBlockState(blockPos))) {

            BlockHelper.playDispenserSound(blockSource);
            BlockHelper.playDispenserAnimation(blockSource, blockSource.state().getValue(DispenserBlock.FACING));

            if (itemStack.getItem() instanceof MobInContainerItem mobInContainerItem) {

                level.playSound(null, blockPos, mobInContainerItem.emptySound, SoundSource.NEUTRAL, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
                mobInContainerItem.checkExtraContent(null, level, itemStack, blockPos);

                DebugHelper.sendDispenserMessage(Chrysalis.LOGGER, Chrysalis.IS_DEBUG, mobInContainerItem.getName().getString(), blockPos);
                return new ItemStack(mobInContainerItem.returnItem.asItem());

            } else if (itemStack.getItem() instanceof MobInFluidBucketItem mobInFluidBucketItem) {

                mobInFluidBucketItem.emptyContents(null, level, blockPos, null, itemStack);
                mobInFluidBucketItem.checkExtraContent(null, level, itemStack, blockPos);

                DebugHelper.sendDispenserMessage(Chrysalis.LOGGER, Chrysalis.IS_DEBUG, mobInFluidBucketItem.getName().getString(), blockPos);
                return new ItemStack(Items.BUCKET);
            }
        }

        return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}