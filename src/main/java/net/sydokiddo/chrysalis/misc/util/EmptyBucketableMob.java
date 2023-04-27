package net.sydokiddo.chrysalis.misc.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import java.util.Optional;

@SuppressWarnings("ALL")
public interface EmptyBucketableMob {

    void setFromBucket(boolean var1);

    void saveToBucketTag(ItemStack var1);

    void loadFromBucketTag(CompoundTag var1);

    ItemStack getBucketItemStack();

    SoundEvent getPickupSound();

    @SuppressWarnings("ALL")
    @Deprecated
    static void saveDefaultDataToBucketTag(Mob mob, ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (mob.hasCustomName()) {
            itemStack.setHoverName(mob.getCustomName());
        }
        if (mob.isNoAi()) {
            compoundTag.putBoolean("NoAI", mob.isNoAi());
        }
        if (mob.isSilent()) {
            compoundTag.putBoolean("Silent", mob.isSilent());
        }
        if (mob.isNoGravity()) {
            compoundTag.putBoolean("NoGravity", mob.isNoGravity());
        }
        if (mob.hasGlowingTag()) {
            compoundTag.putBoolean("Glowing", true);
        }
        if (mob.isInvulnerable()) {
            compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
        }
        compoundTag.putFloat("Health", mob.getHealth());
    }

    @SuppressWarnings("ALL")
    @Deprecated
    static void loadDefaultDataFromBucketTag(Mob mob, CompoundTag compoundTag) {
        if (compoundTag.contains("NoAI")) {
            mob.setNoAi(compoundTag.getBoolean("NoAI"));
        }
        if (compoundTag.contains("Silent")) {
            mob.setSilent(compoundTag.getBoolean("Silent"));
        }
        if (compoundTag.contains("NoGravity")) {
            mob.setNoGravity(compoundTag.getBoolean("NoGravity"));
        }
        if (compoundTag.contains("Glowing")) {
            mob.setGlowingTag(compoundTag.getBoolean("Glowing"));
        }
        if (compoundTag.contains("Invulnerable")) {
            mob.setInvulnerable(compoundTag.getBoolean("Invulnerable"));
        }
        if (compoundTag.contains("Health", 99)) {
            mob.setHealth(compoundTag.getFloat("Health"));
        }
    }

    static <T extends LivingEntity> Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand interactionHand, T livingEntity) {

        ItemStack itemStack = player.getItemInHand(interactionHand);
        Level level = livingEntity.level;
        ItemStack itemStack2 = ((EmptyBucketableMob) livingEntity).getBucketItemStack();

        if (itemStack.getItem() == Items.BUCKET && livingEntity.isAlive()) {

            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, itemStack2);
            }

            livingEntity.playSound(((EmptyBucketableMob) livingEntity).getPickupSound(), 1.0f, 1.0f);
            ((EmptyBucketableMob) livingEntity).saveToBucketTag(itemStack2);
            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
            player.setItemInHand(interactionHand, itemStack3);
            livingEntity.discard();

            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        }
        return Optional.empty();
    }
}