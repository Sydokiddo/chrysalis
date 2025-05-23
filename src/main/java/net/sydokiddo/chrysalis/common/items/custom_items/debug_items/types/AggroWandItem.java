package net.sydokiddo.chrysalis.common.items.custom_items.debug_items.types;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.shared_classes.DebugUtilityItem;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.common.items.custom_items.debug_items.shared_classes.ExtraReachDebugUtilityItem;
import net.sydokiddo.chrysalis.common.misc.CSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class AggroWandItem extends ExtraReachDebugUtilityItem {

    public AggroWandItem(Properties properties) {
        super(properties);
    }

    // region Components and Tooltips

    private static final String
        mobNameString = "mob_name",
        mobUuidString = "mob_uuid"
    ;

    private static boolean hasMobName(ItemStack itemStack) {
        return ItemHelper.getSavedEntityData(itemStack).contains(mobNameString);
    }

    public static boolean hasMobUUID(ItemStack itemStack) {
        return ItemHelper.getSavedEntityData(itemStack).contains(mobUuidString);
    }

    /**
     * Adds a custom tooltip to the aggro wand item.
     **/

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {

        if (hasMobUUID(itemStack)) {
            String mobName = hasMobName(itemStack) ? ItemHelper.getSavedEntityData(itemStack).copyTag().getString(mobNameString) : ComponentHelper.UNKNOWN.getString();
            list.add(Component.translatable(this.getDescriptionId() + ".linked_mob_tooltip", mobName).withStyle(ChatFormatting.GRAY));
        }

        ItemHelper.addUseTooltip(list);
        String descriptionString = hasMobUUID(itemStack) ? ".description_linked" : ".description_unlinked";
        list.add(CommonComponents.space().append(Component.translatable(this.getDescriptionId() + descriptionString).withStyle(ChatFormatting.BLUE)));
    }

    // endregion

    // region Interactions

    /**
     * When right-clicked on a mob, the mob will be bound to the item, and then the next mob that is right-clicked on will be set as the previous mob's attack target.
     **/

    public static InteractionResult doInteraction(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {

        if (player instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide() && !serverPlayer.isShiftKeyDown() && livingEntity instanceof Mob clickedMob) {

            if (hasMobUUID(itemStack)) {

                Entity entity = serverPlayer.serverLevel().getEntity(ItemHelper.getSavedEntityData(itemStack).copyTag().getUUID(mobUuidString));

                if (entity instanceof Mob linkedMob) {

                    addSparkleParticles(clickedMob);
                    addParticlesAroundEntity(linkedMob, ParticleTypes.ANGRY_VILLAGER, 5, 1.5D);

                    linkedMob.setTarget(clickedMob);

                    Brain<?> brain = linkedMob.getBrain();
                    brain.setMemory(MemoryModuleType.ATTACK_TARGET, clickedMob);
                    brain.setMemory(MemoryModuleType.ANGRY_AT, clickedMob.getUUID());
                    brain.setActiveActivityIfPossible(Activity.FIGHT);

                    if (linkedMob instanceof Goat) {
                        brain.setMemory(MemoryModuleType.RAM_COOLDOWN_TICKS, 0);
                        brain.setMemory(MemoryModuleType.RAM_TARGET, clickedMob.getBoundingBox().getCenter());
                    }

                    if (linkedMob instanceof Warden warden && !warden.isDiggingOrEmerging() && warden.canTargetEntity(clickedMob)) {
                        warden.increaseAngerAt(clickedMob, AngerLevel.ANGRY.getMinimumAnger() + 20, false);
                        warden.setAttackTarget(clickedMob);
                    }

                    playSound(serverPlayer, CSoundEvents.AGGRO_WAND_SELECT_TARGET_SUCCESS.get());
                    DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("gui.chrysalis.aggro_wand.select_target_message.success", clickedMob.getName().getString(), linkedMob.getName().getString()));

                } else {
                    playSound(serverPlayer, CSoundEvents.AGGRO_WAND_SELECT_TARGET_FAIL.get());
                    DebugUtilityItem.sendFeedbackMessage(false, serverPlayer, Component.translatable("gui.chrysalis.aggro_wand.select_target_message.fail").withStyle(ChatFormatting.RED));
                }

                removeComponents(itemStack);

            } else {

                addSparkleParticles(clickedMob);
                itemStack.set(DataComponents.ITEM_NAME, Component.translatable(itemStack.getItem().getDescriptionId() + ".linked", clickedMob.getName().getString()));

                CustomData.update(ItemHelper.SAVED_ENTITY_DATA_COMPONENT, itemStack, (compoundTag) -> {
                    compoundTag.putString(mobNameString, clickedMob.getName().getString());
                    compoundTag.putUUID(mobUuidString, clickedMob.getUUID());
                });

                playSound(serverPlayer, CSoundEvents.AGGRO_WAND_LINK.get());
                DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("gui.chrysalis.aggro_wand.link_message", clickedMob.getName().getString()));
            }

            return useItem(serverPlayer, itemStack, interactionHand);
        }

        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (itemStack.has(ItemHelper.SAVED_ENTITY_DATA_COMPONENT) && player instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide() && serverPlayer.isShiftKeyDown()) {
            removeComponents(itemStack);
            playSound(serverPlayer, CSoundEvents.AGGRO_WAND_REMOVE_LINKED_MOB.get());
            DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("gui.chrysalis.aggro_wand.remove_linked_mob_message").withStyle(ChatFormatting.RED));
            return useItem(serverPlayer, itemStack, interactionHand);
        }

        return super.use(level, player, interactionHand);
    }

    private static InteractionResult useItem(ServerPlayer serverPlayer, ItemStack itemStack, InteractionHand interactionHand) {
        serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        serverPlayer.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
        return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(serverPlayer.getItemInHand(interactionHand));
    }

    // endregion

    // region Miscellaneous

    private static void playSound(ServerPlayer serverPlayer, SoundEvent soundEvent) {
        serverPlayer.playNotifySound(soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private static void removeComponents(ItemStack itemStack) {
        itemStack.set(DataComponents.ITEM_NAME, Component.translatable(itemStack.getItem().getDescriptionId()));
        itemStack.remove(ItemHelper.SAVED_ENTITY_DATA_COMPONENT);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldDisplayCrosshair(Player player) {
        return super.shouldDisplayCrosshair(player) && Minecraft.getInstance().crosshairPickEntity instanceof Mob && !player.isShiftKeyDown();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getCrosshairTextureLocation() {
        return Chrysalis.resourceLocationId("hud/aggro_wand_crosshair");
    }

    // endregion
}