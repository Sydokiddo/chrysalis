package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.sydokiddo.chrysalis.util.entities.ContainerMob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobInContainerItem extends Item implements DispensibleContainerItem {

    /**
     * The base class for any mob container item that doesn't place a fluid alongside it.
     **/

    private final EntityType<?> entityType;
    public final SoundEvent emptySound;
    public final Item returnItem;

    public MobInContainerItem(EntityType<? extends Mob> entityType, SoundEvent emptySound, Properties properties) {
        super(properties);
        this.entityType = entityType;
        this.emptySound = emptySound;
        this.returnItem = this.getCraftingRemainder().getItem();
    }

    /**
     * Spawns the mob as long as open space exists when the item is used on a block.
     **/

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext useOnContext) {

        Player player = useOnContext.getPlayer();
        assert player != null;
        ItemStack itemStack = player.getItemInHand(useOnContext.getHand());
        Level level = useOnContext.getLevel();

        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        Direction direction = blockHitResult.getDirection();

        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockPos relativePos = blockPos.relative(direction);

        if (blockHitResult.getType() != HitResult.Type.BLOCK || !level.mayInteract(player, blockPos) || !player.mayUseItemAt(relativePos, direction, itemStack)) {
            return InteractionResult.PASS;
        } else {

            this.checkExtraContent(player, level, itemStack, relativePos);
            player.awardStat(Stats.ITEM_USED.get(this));
            level.playSound(null, relativePos.getX(), relativePos.getY(), relativePos.getZ(), this.emptySound, SoundSource.NEUTRAL, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);

            if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, relativePos, itemStack);
            if (!player.getAbilities().instabuild) player.setItemInHand(useOnContext.getHand(), ItemUtils.createFilledResult(itemStack, player, new ItemStack(this.returnItem)));

            return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
        }
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack itemStack, @NotNull BlockPos blockPos) {
        if (level instanceof ServerLevel serverLevel) {

            Entity entity = this.entityType.spawn(serverLevel, itemStack, null, blockPos, EntitySpawnReason.BUCKET, true, false);

            if (entity instanceof ContainerMob containerMob) {
                containerMob.loadFromItemTag(itemStack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag());
                containerMob.setFromItem(true);
            }

            serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
        }
    }

    @Override
    public boolean emptyContents(@Nullable Player player, @NotNull Level level, @NotNull BlockPos blockPos, @Nullable BlockHitResult blockHitResult) {
        return blockHitResult != null && this.emptyContents(player, level, blockHitResult.getBlockPos().relative(blockHitResult.getDirection()), null);
    }
}