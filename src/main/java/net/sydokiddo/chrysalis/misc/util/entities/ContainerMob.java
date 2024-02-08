package net.sydokiddo.chrysalis.misc.util.entities;

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

        // region Default Mob Tags

        if (mob.hasCustomName()) itemStack.setHoverName(mob.getCustomName());

        if (mob.isCustomNameVisible()) compoundTag.putBoolean("CustomNameVisible", true);

        if (mob.isNoAi()) compoundTag.putBoolean("NoAI", true);

        if (mob.isSilent()) compoundTag.putBoolean("Silent", true);

        if (mob.isNoGravity()) compoundTag.putBoolean("NoGravity", true);

        if (mob.hasGlowingTag()) compoundTag.putBoolean("Glowing", true);

        if (mob.isInvulnerable()) compoundTag.putBoolean("Invulnerable", true);

        compoundTag.putFloat("Health", mob.getHealth());

        // endregion

        // region Animal Tags

        if (mob instanceof AgeableMob ageableMob) compoundTag.putInt("Age", ageableMob.getAge());

        if (mob instanceof Animal animal) compoundTag.putInt("InLove", animal.getInLoveTime());

        // endregion
    }

    static void loadDefaultDataFromItemTag(Mob mob, CompoundTag compoundTag) {

        String health = "Health";

        // region Default Mob Tags

        if (compoundTag.contains("CustomNameVisible")) mob.setCustomNameVisible(true);

        if (compoundTag.contains("NoAI")) mob.setNoAi(true);

        if (compoundTag.contains("Silent")) mob.setSilent(true);

        if (compoundTag.contains("NoGravity")) mob.setNoGravity(true);

        if (compoundTag.contains("Glowing")) mob.setGlowingTag(true);

        if (compoundTag.contains("Invulnerable")) mob.setInvulnerable(true);

        if (compoundTag.contains(health, 99)) mob.setHealth(compoundTag.getFloat(health));

        // endregion

        // region Animal Tags

        if (mob instanceof AgeableMob ageableMob) ageableMob.setAge(compoundTag.getInt("Age"));

        if (mob instanceof Animal animal) animal.setInLoveTime(compoundTag.getInt("InLove"));

        // endregion
    }

    /**
     * Saves the mob to the item stack when picked up.
     **/

    private static boolean doMobContainerPickUp(Player player, Item containerItem, InteractionHand interactionHand, LivingEntity livingEntity) {

        ItemStack itemInHand = player.getItemInHand(interactionHand);
        ItemStack resultItemStack = ((ContainerMob) livingEntity).getResultItemStack();

        if (itemInHand.getItem() == containerItem.asItem() && livingEntity.isAlive()) {

            if (!livingEntity.level().isClientSide()) CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, resultItemStack);
            if (livingEntity instanceof Mob mob && mob.isLeashed()) mob.dropLeash(true, true);

            livingEntity.playSound(((ContainerMob) livingEntity).getPickupSound(), 1.0F, 1.0F);
            ((ContainerMob) livingEntity).saveToItemTag(resultItemStack);
            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemInHand, player, resultItemStack, false));

            livingEntity.discard();
        }

        return true;
    }

    static <T extends LivingEntity> Optional<InteractionResult> containerMobPickup(Player player, InteractionHand interactionHand, T livingEntity, Item usedItem) {
        if (doMobContainerPickUp(player, usedItem.asItem(), interactionHand, livingEntity)) {
            return Optional.of(InteractionResult.sidedSuccess(livingEntity.level().isClientSide()));
        } else {
            return Optional.empty();
        }
    }
 }