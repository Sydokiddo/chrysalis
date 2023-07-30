package net.sydokiddo.chrysalis.misc.util.mobs;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import java.util.Optional;

@SuppressWarnings("ALL")
public interface ContainerMob {

    boolean fromItem();
    void setFromItem(boolean var1);
    void saveToItemTag(ItemStack var1);
    void loadFromItemTag(CompoundTag var1);

    ItemStack getResultItemStack();
    SoundEvent getPickupSound();

    /**
     * Gets the NBT of the mob and saves it to the item stack.
     **/

    static void saveDefaultDataToItemTag(Mob mob, ItemStack itemStack) {

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
            compoundTag.putBoolean("Glowing", mob.hasGlowingTag());
        }
        if (mob.isInvulnerable()) {
            compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
        }
        compoundTag.putFloat("Health", mob.getHealth());
    }

    static void loadDefaultDataFromItemTag(Mob mob, CompoundTag compoundTag) {
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

    /**
     * Saves the mob to the item stack when picked up.
     * <p>
     * Mobs can be picked up in either a Bucket, a Lava Bucket, a Powder Snow Bucket, a Milk Bucket, or a Glass Bottle.
     * <p>
     * Water Buckets are not needed here since the vanilla Bucketable class already allows for this.
     **/

    private static void doContainerMobPickup(Player player, InteractionHand interactionHand, LivingEntity livingEntity) {

        ItemStack itemStack = player.getItemInHand(interactionHand);
        ItemStack resultItemStack = ((ContainerMob) livingEntity).getResultItemStack();

        livingEntity.playSound(((ContainerMob) livingEntity).getPickupSound(), 1.0f, 1.0f);
        ((ContainerMob) livingEntity).saveToItemTag(resultItemStack);
        ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, resultItemStack, false);
        player.setItemInHand(interactionHand, filledResult);

        livingEntity.discard();
    }

    static <T extends LivingEntity> Optional<InteractionResult> emptyBucketMobPickup(Player player, InteractionHand interactionHand, T livingEntity) {

        Item usedItemStack = (Items.BUCKET).asItem();

        ItemStack itemStack = player.getItemInHand(interactionHand);
        Level level = livingEntity.level();
        ItemStack resultItemStack = ((ContainerMob) livingEntity).getResultItemStack();

        if (itemStack.getItem() == usedItemStack && livingEntity.isAlive()) {

            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, resultItemStack);
            }

            doContainerMobPickup(player, interactionHand, livingEntity);
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));

        } else {
            return Optional.empty();
        }
    }

    static <T extends LivingEntity> Optional<InteractionResult> lavaBucketMobPickup(Player player, InteractionHand interactionHand, T livingEntity) {

        Item usedItemStack = (Items.LAVA_BUCKET).asItem();

        ItemStack itemStack = player.getItemInHand(interactionHand);
        Level level = livingEntity.level();
        ItemStack resultItemStack = ((ContainerMob) livingEntity).getResultItemStack();

        if (itemStack.getItem() == usedItemStack && livingEntity.isAlive()) {

            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, resultItemStack);
            }

            doContainerMobPickup(player, interactionHand, livingEntity);
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));

        } else {
            return Optional.empty();
        }
    }

    static <T extends LivingEntity> Optional<InteractionResult> powderSnowBucketMobPickup(Player player, InteractionHand interactionHand, T livingEntity) {

        Item usedItemStack = (Items.POWDER_SNOW_BUCKET).asItem();

        ItemStack itemStack = player.getItemInHand(interactionHand);
        Level level = livingEntity.level();
        ItemStack resultItemStack = ((ContainerMob) livingEntity).getResultItemStack();

        if (itemStack.getItem() == usedItemStack && livingEntity.isAlive()) {

            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, resultItemStack);
            }

            doContainerMobPickup(player, interactionHand, livingEntity);
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));

        } else {
            return Optional.empty();
        }
    }

    static <T extends LivingEntity> Optional<InteractionResult> milkBucketMobPickup(Player player, InteractionHand interactionHand, T livingEntity) {

        Item usedItemStack = (Items.MILK_BUCKET).asItem();

        ItemStack itemStack = player.getItemInHand(interactionHand);
        Level level = livingEntity.level();
        ItemStack resultItemStack = ((ContainerMob) livingEntity).getResultItemStack();

        if (itemStack.getItem() == usedItemStack && livingEntity.isAlive()) {

            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, resultItemStack);
            }

            doContainerMobPickup(player, interactionHand, livingEntity);
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));

        } else {
            return Optional.empty();
        }
    }

    static <T extends LivingEntity> Optional<InteractionResult> emptyBottleMobPickup(Player player, InteractionHand interactionHand, T livingEntity) {

        Item usedItemStack = (Items.GLASS_BOTTLE).asItem();

        ItemStack itemStack = player.getItemInHand(interactionHand);
        Level level = livingEntity.level();
        ItemStack resultItemStack = ((ContainerMob) livingEntity).getResultItemStack();

        if (itemStack.getItem() == usedItemStack && livingEntity.isAlive()) {

            if (!level.isClientSide) {
                CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger((ServerPlayer) player, itemStack, livingEntity);
            }

            doContainerMobPickup(player, interactionHand, livingEntity);
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));

        } else {
            return Optional.empty();
        }
    }
}