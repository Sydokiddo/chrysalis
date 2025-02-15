package net.sydokiddo.chrysalis.misc.util.dispenser;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.helpers.BlockHelper;
import net.sydokiddo.chrysalis.registry.items.custom_items.CSpawnEggItem;
import org.jetbrains.annotations.NotNull;

public class DispenseCustomSpawnEggDispenserBehavior implements DispenseItemBehavior {

    /**
     * Dispenses any custom spawn egg item.
     **/

    public static final DispenseCustomSpawnEggDispenserBehavior INSTANCE = new DispenseCustomSpawnEggDispenserBehavior();

    @Override
    public @NotNull ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {

        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        EntityType<?> entityType = ((CSpawnEggItem) itemStack.getItem()).getType(blockSource.level().registryAccess(), itemStack);

        try {
            entityType.spawn(blockSource.level(), itemStack, null, blockSource.pos().relative(direction), EntitySpawnReason.DISPENSER, direction != Direction.UP, false);
        } catch (Exception exception) {
            LOGGER.error("Error while dispensing spawn egg from dispenser at {}", blockSource.pos(), exception);
            return ItemStack.EMPTY;
        }

        BlockHelper.playDispenserSound(blockSource);
        BlockHelper.playDispenserAnimation(blockSource, direction);

        itemStack.shrink(1);
        blockSource.level().gameEvent(null, GameEvent.ENTITY_PLACE, blockSource.pos());
        return itemStack;
    }
}