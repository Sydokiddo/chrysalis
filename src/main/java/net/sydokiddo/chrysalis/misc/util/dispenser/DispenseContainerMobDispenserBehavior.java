package net.sydokiddo.chrysalis.misc.util.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.helpers.BlockHelper;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInContainerItem;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInFluidBucketItem;
import org.jetbrains.annotations.NotNull;

public class DispenseContainerMobDispenserBehavior implements DispenseItemBehavior {

    public static final DispenseContainerMobDispenserBehavior INSTANCE = new DispenseContainerMobDispenserBehavior();

    /**
     * Dispenses any mob container item.
     **/

    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {

        BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
        ServerLevel level = blockSource.level();

        if (BlockHelper.isBlockStateFree(level.getBlockState(blockPos))) {

            BlockHelper.playDispenserSound(blockSource);
            BlockHelper.playDispenserAnimation(blockSource, blockSource.state().getValue(DispenserBlock.FACING));
            level.gameEvent(GameEvent.ENTITY_PLACE, blockPos, GameEvent.Context.of(blockSource.state()));

            if (itemStack.getItem() instanceof MobInContainerItem mobInContainerItem) {

                level.playSound(null, blockPos, mobInContainerItem.emptySound, SoundSource.NEUTRAL, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
                mobInContainerItem.checkExtraContent(null, level, itemStack, blockPos);

                return new ItemStack(mobInContainerItem.returnItem.asItem());

            } else if (itemStack.getItem() instanceof MobInFluidBucketItem mobInFluidBucketItem) {

                mobInFluidBucketItem.emptyContents(null, level, blockPos, null);
                mobInFluidBucketItem.checkExtraContent(null, level, itemStack, blockPos);

                return new ItemStack(Items.BUCKET);
            }
        }

        return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}