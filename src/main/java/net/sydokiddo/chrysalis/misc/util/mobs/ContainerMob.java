package net.sydokiddo.chrysalis.misc.util.mobs;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ContainerMob {

    boolean fromItem();
    void setFromItem(boolean fromItem);
    void saveToItemTag(ItemStack itemStack);
    void loadFromItemTag(CompoundTag compoundTag);

    ItemStack getResultItemStack();
    SoundEvent getPickupSound();

    /**
     * Gets the NBT of the mob and saves it to the item stack.
     **/

    static void saveDefaultDataToItemTag(Mob mob, ItemStack itemStack) {

        CompoundTag compoundTag = itemStack.getOrCreateTag();

        // Saves the default mob tags

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

        // If the mob is an ageable mob, save the mob's age

        if (mob instanceof AgeableMob ageableMob) {
            compoundTag.putInt("Age", ageableMob.getAge());
        }

        // If the mob is an animal, save the mob's in love time

        if (mob instanceof Animal animal) {
            compoundTag.putInt("InLove", animal.getInLoveTime());
        }
    }

    static void loadDefaultDataFromItemTag(Mob mob, CompoundTag compoundTag) {

        // Loads the default mob tags

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

        // If the mob is an ageable mob, load the mob's age

        if (mob instanceof AgeableMob ageableMob) {
            ageableMob.setAge(compoundTag.getInt("Age"));
        }

        // If the mob is an animal, load the mob's in love time

        if (mob instanceof Animal animal) {
            animal.setInLoveTime(compoundTag.getInt("InLove"));
        }
    }

    /**
     * Saves the mob to the item stack when picked up.
     **/

    private static boolean doMobContainerPickUp(Player player, Item containerItem, InteractionHand interactionHand, LivingEntity livingEntity) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);
        ItemStack resultItemStack = ((ContainerMob) livingEntity).getResultItemStack();

        if (itemInHand.getItem() == containerItem.asItem() && livingEntity.isAlive()) {

            if (!livingEntity.level().isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, resultItemStack);
            }

            livingEntity.playSound(((ContainerMob) livingEntity).getPickupSound(), 1.0F, 1.0F);
            ((ContainerMob) livingEntity).saveToItemTag(resultItemStack);
            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemInHand, player, resultItemStack, false));

            livingEntity.discard();
        }
        return true;
    }

    static <T extends LivingEntity> Optional<InteractionResult> containerMobPickup(Player player, InteractionHand interactionHand, T livingEntity, Item usedItem) {
        if (doMobContainerPickUp(player, usedItem.asItem(), interactionHand, livingEntity)) {
            return Optional.of(InteractionResult.sidedSuccess(livingEntity.level().isClientSide));
        } else {
            return Optional.empty();
        }
    }
 }