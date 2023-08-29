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
     **/

    private static boolean doMobContainerPickUp(Player player, Item containerItem, InteractionHand interactionHand, LivingEntity livingEntity) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);
        Item usedItem = containerItem.asItem();
        ItemStack resultItemStack = ((ContainerMob) livingEntity).getResultItemStack();

        if (itemInHand.getItem() == usedItem && livingEntity.isAlive()) {

            if (!livingEntity.level().isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, resultItemStack);
            }

            livingEntity.playSound(((ContainerMob) livingEntity).getPickupSound(), 1.0f, 1.0f);
            ((ContainerMob) livingEntity).saveToItemTag(resultItemStack);
            ItemStack filledResult = ItemUtils.createFilledResult(itemInHand, player, resultItemStack, false);
            player.setItemInHand(interactionHand, filledResult);

            livingEntity.discard();
        }
        return true;
    }

    static <T extends LivingEntity> Optional<InteractionResult> containerMobPickup(Player player, InteractionHand interactionHand, T livingEntity, Item usedItem) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);
        boolean bl = doMobContainerPickUp(player, usedItem.asItem(), interactionHand, livingEntity);

        if (bl && itemInHand.getItem() == usedItem) {
            return Optional.of(InteractionResult.sidedSuccess(livingEntity.level().isClientSide));
        } else {
            return Optional.empty();
        }
    }
 }