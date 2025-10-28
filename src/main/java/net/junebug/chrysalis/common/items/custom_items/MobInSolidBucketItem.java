package net.junebug.chrysalis.common.items.custom_items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobInSolidBucketItem extends MobInContainerItem {

    /**
     * The base class for any mob container item that places a block alongside it.
     **/

    private final Block blockType;

    public MobInSolidBucketItem(EntityType<? extends Mob> entityType, Block blockType, SoundEvent emptySound, Properties properties) {
        super(entityType, emptySound, properties);
        this.blockType = blockType;
    }

    /**
     * Places a block alongside spawning the mob in the container.
     **/

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack itemStack, @NotNull BlockPos blockPos) {

        super.checkExtraContent(player, level, itemStack, blockPos);

        if (level.isInWorldBounds(blockPos) && level.isEmptyBlock(blockPos)) {
            level.gameEvent(player, GameEvent.FLUID_PLACE, blockPos);
            if (!level.isClientSide()) level.setBlockAndUpdate(blockPos, this.blockType.defaultBlockState());
        }
    }
}