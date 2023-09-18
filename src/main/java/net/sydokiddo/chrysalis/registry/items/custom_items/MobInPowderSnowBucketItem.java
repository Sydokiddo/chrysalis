package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ALL")
public class MobInPowderSnowBucketItem extends MobInContainerItem {

    public MobInPowderSnowBucketItem(EntityType<?> entityType, SoundEvent emptySound, Properties properties, Item returnItem) {
        super(entityType, emptySound, properties, returnItem);
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack itemStack, @NotNull BlockPos blockPos) {

        super.checkExtraContent(player, level, itemStack, blockPos);

        if (level.isInWorldBounds(blockPos) && level.isEmptyBlock(blockPos)) {

            level.gameEvent(player, GameEvent.FLUID_PLACE, blockPos);

            if (!level.isClientSide) {
                level.setBlock(blockPos, Blocks.POWDER_SNOW.defaultBlockState(), 3);
            }
        }
    }
}