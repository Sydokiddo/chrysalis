package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.sydokiddo.chrysalis.misc.util.entities.ContainerMob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobInContainerItem extends Item implements DispensibleContainerItem {

    private final EntityType<?> type;
    public final SoundEvent emptySound;
    public final Item returnItem;

    public MobInContainerItem(EntityType<?> entityType, SoundEvent emptySound, Properties properties, Item returnItem) {
        super(properties);
        this.type = entityType;
        this.emptySound = emptySound;
        this.returnItem = returnItem;
    }

    /**
     * Spawns the mob as long as open space exists when the item is used on a block
     **/

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext useOnContext) {

        Player player = useOnContext.getPlayer();
        assert player != null;
        ItemStack itemStack = player.getItemInHand(useOnContext.getHand());
        Level level = useOnContext.getLevel();

        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos usePos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());

        if (blockHitResult.getType() == HitResult.Type.MISS || blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        } else {

            this.checkExtraContent(player, level, itemStack, usePos);
            player.awardStat(Stats.ITEM_USED.get(this));
            level.playSound(null, usePos.getX(), usePos.getY(), usePos.getZ(), emptySound, SoundSource.NEUTRAL, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);

            if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, usePos, itemStack);
            if (!player.getAbilities().instabuild) player.setItemInHand(useOnContext.getHand(), ItemUtils.createFilledResult(itemStack, player, new ItemStack(returnItem)));

            return InteractionResult.sidedSuccess(level.isClientSide());
        }
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack itemStack, @NotNull BlockPos blockPos) {
        if (level instanceof ServerLevel) {
            this.spawn((ServerLevel)level, itemStack, blockPos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
        }
    }

    @Override
    public boolean emptyContents(@Nullable Player player, @NotNull Level level, @NotNull BlockPos blockPos, @Nullable BlockHitResult blockHitResult) {
        level.getBlockState(blockPos);
        return blockHitResult != null && this.emptyContents(player, level, blockHitResult.getBlockPos().relative(blockHitResult.getDirection()), null);
    }

    private void spawn(ServerLevel serverLevel, ItemStack itemStack, BlockPos blockPos) {

        Entity entity = this.type.spawn(serverLevel, itemStack, null, blockPos, MobSpawnType.BUCKET, true, false);

        if (entity instanceof ContainerMob containerMob) {
            if (containerMob instanceof Mob mob) mob.setPersistenceRequired();
            containerMob.loadFromItemTag(itemStack.getOrCreateTag());
            containerMob.setFromItem(true);
        }
    }
}