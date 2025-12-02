package net.junebug.chrysalis.common.items.custom_items.debug_items.types;

import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.items.custom_items.debug_items.shared_classes.DebugUtilityItem;
import net.junebug.chrysalis.common.misc.CSoundEvents;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.util.helpers.EntityHelper;
import net.junebug.chrysalis.util.helpers.ItemHelper;
import net.junebug.chrysalis.util.helpers.RegistryHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class EventTestItem extends DebugUtilityItem {

    /**
     * The base class for event test items, items that can be used to test out various specified events added by chrysalis.
     **/

    private final String description;

    public EventTestItem(String name) {
        super(RegistryHelper.debugUtilityProperties(64).setId(ResourceKey.create(Registries.ITEM, Chrysalis.resourceLocationId(name))));
        this.description = "item." + Chrysalis.MOD_ID + "." + name + ".description";
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack itemStack) {
        return Component.translatable("item.chrysalis.event_test", Component.translatable(this.description));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(Component.translatable(this.description).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        ItemHelper.addUseTooltip(list);
        list.add(CommonComponents.space().append(Component.translatable("item.chrysalis.event_test.description").withStyle(ChatFormatting.BLUE)));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {

        if (this.emitEvent(level, player, interactionHand) && player instanceof ServerPlayer serverPlayer) {

            EntityHelper.playItemUseNotifySound(serverPlayer, CSoundEvents.EVENT_TEST_USE.get());
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
            DebugUtilityItem.sendFeedbackMessage(true, serverPlayer, Component.translatable("gui.chrysalis.event_test.message", Component.translatable(this.description)));
            ItemHelper.addCooldownToTag(serverPlayer, CTags.EVENT_TEST_ITEMS, 25);

            return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(player.getItemInHand(interactionHand));
        }

        else return super.use(level, player, interactionHand);
    }

    public boolean emitEvent(Level level, Player player, @NotNull InteractionHand interactionHand) {
        return false;
    }
}