package net.sydokiddo.chrysalis.util.blocks.dispensers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.CopyingSpawnEggItem;
import net.sydokiddo.chrysalis.util.helpers.BlockHelper;
import net.sydokiddo.chrysalis.common.items.custom_items.CustomSpawnEggItem;
import net.sydokiddo.chrysalis.util.helpers.DebugHelper;
import org.jetbrains.annotations.NotNull;

public class DispenseCustomSpawnEggDispenserBehavior implements DispenseItemBehavior {

    /**
     * Dispenses any custom spawn egg item.
     **/

    public static final DispenseCustomSpawnEggDispenserBehavior INSTANCE = new DispenseCustomSpawnEggDispenserBehavior();

    @Override
    public @NotNull ItemStack dispense(@NotNull BlockSource blockSource, ItemStack itemStack) {

        if (!(itemStack.getItem() instanceof CustomSpawnEggItem customSpawnEggItem) || itemStack.getItem() instanceof CopyingSpawnEggItem && CopyingSpawnEggItem.getCustomData(itemStack).isEmpty()) return BlockHelper.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        EntityType<?> entityType = customSpawnEggItem.getType(blockSource.level().registryAccess(), itemStack);
        BlockPos blockPos = blockSource.pos().relative(direction);

        try {
            DebugHelper.sendDispenserMessage(Chrysalis.LOGGER, Chrysalis.IS_DEBUG, entityType.toString(), blockPos);
            entityType.spawn(blockSource.level(), itemStack, null, blockPos, EntitySpawnReason.DISPENSER, direction != Direction.UP, false);
        } catch (Exception exception) {
            Chrysalis.LOGGER.error("Error while dispensing custom spawn egg from dispenser at {}", blockSource.pos(), exception);
            return ItemStack.EMPTY;
        }

        BlockHelper.playDispenserSound(blockSource);
        BlockHelper.playDispenserAnimation(blockSource, direction);

        itemStack.shrink(1);
        blockSource.level().gameEvent(null, GameEvent.ENTITY_PLACE, blockSource.pos());
        return itemStack;
    }
}