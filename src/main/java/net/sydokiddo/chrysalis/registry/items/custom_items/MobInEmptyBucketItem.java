package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.chrysalis.misc.util.EmptyBucketableMob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ALL")
public class MobInEmptyBucketItem extends Item implements DispensibleContainerItem {

    private final EntityType<?> type;
    private final SoundEvent emptySound;

    public MobInEmptyBucketItem(EntityType<?> entityType, SoundEvent soundEvent, Item.Properties properties) {
        super(properties);
        this.type = entityType;
        this.emptySound = soundEvent;
    }

    public SoundEvent getEmptySound() {
        return SoundEvents.BUCKET_EMPTY;
    }

    public float getEmptySoundVolume() {
        return 1.0f;
    }

    public float getEmptySoundPitch(Level level) {
        return 0.8f + level.random.nextFloat() * 0.4f;
    }

    @Override
    public void checkExtraContent(@Nullable Player player, @NotNull Level level, @NotNull ItemStack itemStack, @NotNull BlockPos blockPos) {
        if (level instanceof ServerLevel) {
            this.spawn((ServerLevel)level, itemStack, blockPos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
        }
    }

    @Override
    public InteractionResult useOn(@NotNull UseOnContext useOnContext) {

        Player player = useOnContext.getPlayer();
        InteractionHand hand = useOnContext.getHand();
        assert player != null;
        ItemStack itemStack = player.getItemInHand(hand);
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();

        this.checkExtraContent(player, level, itemStack, blockPos);
        level.playSound(null, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), getEmptySound(), SoundSource.NEUTRAL, getEmptySoundVolume(), getEmptySoundPitch(level));

        if (!player.getAbilities().instabuild) {
            player.setItemInHand(useOnContext.getHand(), ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.BUCKET)));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean emptyContents(@Nullable Player player, @NotNull Level level, @NotNull BlockPos blockPos, @Nullable BlockHitResult blockHitResult) {
        level.getBlockState(blockPos);
        this.playEmptySound(player, level, blockPos);
        return blockHitResult != null && this.emptyContents(player, level, blockHitResult.getBlockPos().relative(blockHitResult.getDirection()), null);
    }

    private void playEmptySound(@Nullable Player player, LevelAccessor levelAccessor, @NotNull BlockPos blockPos) {
        levelAccessor.playSound(player, blockPos, this.emptySound, SoundSource.NEUTRAL, 1.0f, 1.0f);
    }

    private void spawn(ServerLevel serverLevel, ItemStack itemStack, BlockPos blockPos) {
        Entity entity = this.type.spawn(serverLevel, itemStack, null, blockPos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof EmptyBucketableMob bucketable) {
            bucketable.loadFromBucketTag(itemStack.getOrCreateTag());
            bucketable.setFromBucket(true);
        }
    }
}