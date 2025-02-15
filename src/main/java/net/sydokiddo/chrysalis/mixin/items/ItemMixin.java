package net.sydokiddo.chrysalis.mixin.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.level.block.LightBlock;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {

    /**
     * Adds tooltips to various items.
     **/

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void chrysalis$addTooltipToItems(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo info) {

        DamageResistant damageResistant = itemStack.get(DataComponents.DAMAGE_RESISTANT);

        if (damageResistant != null && damageResistant.types() == DamageTypeTags.IS_FIRE) {

            MutableComponent fireproofIcon = ChrysalisRegistry.FLAME_ICON;
            int fireproofColor = ChrysalisRegistry.FIRE_COLOR.getRGB();

            if (ItemHelper.nameContains(itemStack, "soul") || ItemHelper.nameContains(itemStack, "Soul")) {
                fireproofIcon = ChrysalisRegistry.SOUL_FLAME_ICON;
                fireproofColor = ChrysalisRegistry.SOUL_FIRE_COLOR.getRGB();
            } else if (ItemHelper.nameContains(itemStack, "memory") || ItemHelper.nameContains(itemStack, "Memory") || ItemHelper.nameContains(itemStack, "memories") || ItemHelper.nameContains(itemStack, "Memories")) {
                fireproofIcon = ChrysalisRegistry.MEMORY_FLAME_ICON;
                fireproofColor = ChrysalisRegistry.MEMORY_FIRE_COLOR.getRGB();
            }

            int finalFireproofColor = fireproofColor;
            ItemHelper.setTooltipIconsFont(fireproofIcon, Chrysalis.MOD_ID);
            Component fireproofTooltip = ItemHelper.addTooltipWithIcon(fireproofIcon, Component.translatable("gui.chrysalis.item.fireproof").withStyle(style -> style.withItalic(true).withColor(finalFireproofColor)));
            list.add(fireproofTooltip);
        }

        if (itemStack.is(ChrysalisTags.WAXED_BLOCK_ITEMS)) {
            MutableComponent waxedIcon = ChrysalisRegistry.WAXED_ICON;
            ItemHelper.setTooltipIconsFont(waxedIcon, Chrysalis.MOD_ID);
            Component waxedTooltip = ItemHelper.addTooltipWithIcon(waxedIcon, Component.translatable("gui.chrysalis.item.waxed").withStyle(style -> style.withItalic(true).withColor(ChrysalisRegistry.WAXED_COLOR.getRGB())));
            list.add(waxedTooltip);
        }

        if (itemStack.is(Items.DEBUG_STICK)) {
            ItemHelper.addUseTooltip(list);
            list.add(CommonComponents.space().append(Component.translatable("item.chrysalis.debug_stick.desc").withStyle(ChatFormatting.BLUE)));
        }

        if (itemStack.is(Items.LIGHT)) {
            BlockItemStateProperties blockItemStateProperties = itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
            list.add(Component.translatable("item.chrysalis.light.desc", blockItemStateProperties.get(LightBlock.LEVEL) != null ? blockItemStateProperties.get(LightBlock.LEVEL) : 15).withStyle(ChatFormatting.GRAY));
        }
    }

    @SuppressWarnings("unused")
    @Mixin(ItemStack.class)
    public static abstract class ItemStackMixin implements DataComponentHolder {

        @Shadow public abstract Item getItem();

        /**
         * Adds a Chrysalis tooltip to any new item registered by the mod.
         **/

        @Environment(EnvType.CLIENT)
        @Inject(method = "getTooltipLines", at = @At("TAIL"))
        private void chrysalis$addModNameTooltip(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
            if (this.getItem().getDescriptionId().contains(Chrysalis.MOD_ID) && !tooltipFlag.isAdvanced() && !this.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)) {
                if (!cir.getReturnValue().isEmpty()) cir.getReturnValue().add(CommonComponents.EMPTY);
                MutableComponent chrysalisIcon = ChrysalisRegistry.CHRYSALIS_ICON;
                ItemHelper.setTooltipIconsFont(chrysalisIcon, Chrysalis.MOD_ID);
                Component chrysalisTooltip = ItemHelper.addTooltipWithIcon(chrysalisIcon, Component.translatable("mod.chrysalis").withStyle(style -> style.withFont(ChrysalisRegistry.FIVE_FONT).withColor(ChrysalisRegistry.CHRYSALIS_COLOR.getRGB())));
                cir.getReturnValue().add(chrysalisTooltip);
            }
        }
    }
}