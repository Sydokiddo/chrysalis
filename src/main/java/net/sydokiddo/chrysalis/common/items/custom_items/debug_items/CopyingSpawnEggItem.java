package net.sydokiddo.chrysalis.common.items.custom_items.debug_items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.custom_items.CustomSpawnEggItem;
import net.sydokiddo.chrysalis.common.misc.ChrysalisSoundEvents;
import net.sydokiddo.chrysalis.common.misc.ChrysalisTags;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CopyingSpawnEggItem extends CustomSpawnEggItem {

    public CopyingSpawnEggItem(Properties properties) {
        super(null, null, properties);
    }

    // region Components and Tooltips

    public static CustomData getCustomData(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponents.ENTITY_DATA, CustomData.EMPTY);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {

        if (!getCustomData(itemStack).isEmpty()) {
            EntityType<?> entityType = getCustomData(itemStack).parseEntityType(Objects.requireNonNull(tooltipContext.registries()), Registries.ENTITY_TYPE);
            String entityName = entityType != null ? entityType.getDescription().getString() : ComponentHelper.UNKNOWN.getString();
            list.add(Component.translatable(this.getDescriptionId() + ".copied_entity_tooltip", entityName).withStyle(ChatFormatting.GRAY));
        }

        ItemHelper.addUseTooltip(list);
        String descriptionString = !getCustomData(itemStack).isEmpty() ? ".description_copied" : ".description_uncopied";
        list.add(CommonComponents.space().append(Component.translatable(this.getDescriptionId() + descriptionString).withStyle(ChatFormatting.BLUE)));
    }

    // endregion

    // region Interactions

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        if (getCustomData(context.getItemInHand()).isEmpty()) return InteractionResult.FAIL;
        return super.useOn(context);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (getCustomData(itemStack).isEmpty()) {
            return InteractionResult.FAIL;
        } else {
            if (player instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide() && serverPlayer.isShiftKeyDown()) {
                itemStack.remove(DataComponents.ENTITY_DATA);
                useItem(player, itemStack, ChrysalisSoundEvents.COPYING_SPAWN_EGG_REMOVE_COPIED_ENTITY.get());
                serverPlayer.sendSystemMessage(createTextComponent(Component.translatable("gui.chrysalis.copying_spawn_egg.remove_message").withStyle(ChatFormatting.RED)), true);
                return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(player.getItemInHand(interactionHand));
            }
        }

        return super.use(level, player, interactionHand);
    }

    public static InteractionResult copyEntity(CopyingSpawnEggItem copyingSpawnEggItem, ItemStack itemStack, Player player, Entity entity, InteractionHand interactionHand) {

        if (player instanceof ServerPlayer serverPlayer && !serverPlayer.level().isClientSide() && !serverPlayer.isShiftKeyDown() && entity.getType() != copyingSpawnEggItem.getType(player.level().registryAccess(), itemStack) && !entity.getType().is(ChrysalisTags.COPYING_SPAWN_EGG_BLACKLISTED)) {
            CustomData.update(DataComponents.ENTITY_DATA, itemStack, (compoundTag) -> compoundTag.putString("id", entity.getType().toShortString()));
            useItem(serverPlayer, itemStack, ChrysalisSoundEvents.COPYING_SPAWN_EGG_COPY_ENTITY.get());
            serverPlayer.sendSystemMessage(createTextComponent(Component.translatable("gui.chrysalis.copying_spawn_egg.copy_message", entity.getType().getDescription().getString())), true);
            return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(player.getItemInHand(interactionHand));
        }

        return InteractionResult.PASS;
    }

    private static void useItem(Player player, ItemStack itemStack, SoundEvent soundEvent) {
        player.playNotifySound(soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
    }

    private static Component createTextComponent(Component component) {
        MutableComponent eggIcon = ComponentHelper.EGG_ICON;
        ComponentHelper.setIconsFont(eggIcon, Chrysalis.MOD_ID);
        return ItemHelper.addTooltipWithIconBothSides(eggIcon, component);
    }

    // endregion

    @Override
    public @NotNull EntityType<?> getType(HolderLookup.@NotNull Provider registries, @NotNull ItemStack itemStack) {

        if (!getCustomData(itemStack).isEmpty()) {
            EntityType<?> entityType = getCustomData(itemStack).parseEntityType(registries, Registries.ENTITY_TYPE);
            if (entityType != null) return entityType;
        }

        return super.getType(registries, itemStack);
    }

    @Override
    public @NotNull Optional<Mob> spawnOffspringFromSpawnEgg(@NotNull Player player, @NotNull Mob mob, @NotNull EntityType<? extends Mob> entityType, @NotNull ServerLevel serverLevel, @NotNull Vec3 vec3, @NotNull ItemStack itemStack) {
        return Optional.empty();
    }

    @Override
    public @NotNull FeatureFlagSet requiredFeatures() {
        return FeatureFlagSet.of();
    }
}