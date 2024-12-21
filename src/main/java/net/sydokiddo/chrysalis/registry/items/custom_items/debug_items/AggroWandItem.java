package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.base_classes.ExtraReachDebugUtilityItem;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import java.util.List;

public class AggroWandItem extends ExtraReachDebugUtilityItem {

    public AggroWandItem(Properties properties) {
        super(properties);
    }

    // region Item Components

    private static final String
        mobNameString = "mob_name",
        mobUuidString = "mob_uuid"
    ;

    private static final DataComponentType<CustomData> customDataComponent = DataComponents.CUSTOM_DATA;

    private static CustomData getCustomData(ItemStack itemStack) {
        return itemStack.getOrDefault(customDataComponent, CustomData.EMPTY);
    }

    private static boolean hasMobName(ItemStack itemStack) {
        return getCustomData(itemStack).contains(mobNameString);
    }

    public static boolean hasMobUUID(ItemStack itemStack) {
        return getCustomData(itemStack).contains(mobUuidString);
    }

    // endregion

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {

        if (hasMobUUID(itemStack)) {
            String mobName = hasMobName(itemStack) ? getCustomData(itemStack).copyTag().getString(mobNameString) : ItemHelper.UNKNOWN_COMPONENT.getString();
            list.add(Component.translatable(this.getDescriptionId() + ".linked_mob_tooltip", mobName).withStyle(ChatFormatting.GRAY));
        }

        ItemHelper.addUseTooltip(list);
        String descriptionString = hasMobUUID(itemStack) ? ".desc_linked" : ".desc_unlinked";
        list.add(CommonComponents.space().append(Component.translatable(this.getDescriptionId() + descriptionString).withStyle(ChatFormatting.BLUE)));
    }

    /**
     * When right-clicked on a mob, the mob will be bound to the item, and then the next mob that is right-clicked on will be set as the previous mob's attack target.
     **/

    public static InteractionResult doInteraction(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {

        if (player instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide() && livingEntity instanceof Mob mob) {

            if (hasMobUUID(itemStack)) {

                Mob linkedMob = (Mob) serverPlayer.serverLevel().getEntity(getCustomData(itemStack).copyTag().getUUID(mobUuidString));

                if (linkedMob != null) {
                    addGlowingEffect(mob);
                    linkedMob.setTarget(livingEntity);
                    serverPlayer.playNotifySound(ChrysalisSoundEvents.AGGRO_WAND_SELECT_TARGET_SUCCESS, SoundSource.PLAYERS, 1.0F, 1.0F);
                    serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.aggro_wand.set_target", livingEntity.getName().getString(), linkedMob.getName().getString()));
                } else {
                    serverPlayer.playNotifySound(ChrysalisSoundEvents.AGGRO_WAND_SELECT_TARGET_FAIL, SoundSource.PLAYERS, 1.0F, 1.0F);
                    serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.aggro_wand.linked_mob_missing"));
                }

                ItemStack unlinkedWand = itemStack.copy();
                unlinkedWand.remove(customDataComponent);
                player.setItemInHand(interactionHand, unlinkedWand);

            } else {

                addGlowingEffect(mob);
                ItemStack linkedWand = itemStack.copy();

                CustomData.update(customDataComponent, linkedWand, (compoundTag) -> {
                    compoundTag.putString(mobNameString, livingEntity.getName().getString());
                    compoundTag.putUUID(mobUuidString, livingEntity.getUUID());
                });

                player.setItemInHand(interactionHand, linkedWand);

                serverPlayer.playNotifySound(ChrysalisSoundEvents.AGGRO_WAND_LINK, SoundSource.PLAYERS, 1.0F, 1.0F);
                serverPlayer.sendSystemMessage(Component.translatable("gui.chrysalis.aggro_wand.link_mob", livingEntity.getName().getString()));
            }

            serverPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            serverPlayer.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
            return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(player.getItemInHand(interactionHand));
        }

        return InteractionResult.PASS;
    }

    private static void addGlowingEffect(Mob mob) {
        mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0, false, false, false));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ResourceLocation getCrosshairTextureLocation() {
        return Chrysalis.id("hud/aggro_wand_crosshair");
    }
}