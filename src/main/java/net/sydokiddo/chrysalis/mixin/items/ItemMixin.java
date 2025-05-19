package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.LightBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.items.CDataComponents;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.common.misc.CTags;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mixin(Item.class)
public class ItemMixin {

    /**
     * Adds tooltips to various items.
     **/

    @SuppressWarnings("deprecation")
    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void chrysalis$addTooltipsToItems(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo info) {

        if (itemStack.has(CDataComponents.REMAINS_ON_DEATH) && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
            MutableComponent remainsOnDeathIcon = ComponentHelper.REMAINS_ON_DEATH_ICON;
            ComponentHelper.setIconsFont(remainsOnDeathIcon, Chrysalis.MOD_ID);
            Component remainsOnDeathTooltip = ItemHelper.addTooltipWithIcon(remainsOnDeathIcon, Component.translatable("gui.chrysalis.item.remains_on_death").withStyle(style -> style.withItalic(true).withColor(ComponentHelper.REMAINS_ON_DEATH_COLOR.getRGB())));
            list.add(remainsOnDeathTooltip);
        }

        if (itemStack.has(CDataComponents.MUFFLED)) {
            MutableComponent muffledIcon = ComponentHelper.MUFFLED_ICON;
            ComponentHelper.setIconsFont(muffledIcon, Chrysalis.MOD_ID);
            Component muffledTooltip = ItemHelper.addTooltipWithIcon(muffledIcon, Component.translatable("gui.chrysalis.item.muffled").withStyle(style -> style.withItalic(true).withColor(ComponentHelper.SOUL_FIRE_COLOR.getRGB())));
            list.add(muffledTooltip);
        }

        if (!CConfigOptions.REWORKED_TOOLTIPS.get()) return;
        DamageResistant damageResistant = itemStack.get(DataComponents.DAMAGE_RESISTANT);

        if (damageResistant != null && damageResistant.types() == DamageTypeTags.IS_FIRE) {

            MutableComponent fireproofIcon = ComponentHelper.FLAME_ICON;
            int fireproofColor = ComponentHelper.FIRE_COLOR.getRGB();

            if (ItemHelper.listContainsName(itemStack, ComponentHelper.SOUL_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.SOUL_FLAME_ICON;
                fireproofColor = ComponentHelper.SOUL_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.MEMORY_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.MEMORY_FLAME_ICON;
                fireproofColor = ComponentHelper.MEMORY_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.VOID_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.VOID_FLAME_ICON;
                fireproofColor = ComponentHelper.VOID_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.TREACHEROUS_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.TREACHEROUS_FLAME_ICON;
                fireproofColor = ComponentHelper.TREACHEROUS_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.NECROTIC_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.NECROTIC_FLAME_ICON;
                fireproofColor = ComponentHelper.NECROTIC_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.PURITY_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.PURITY_FLAME_ICON;
                fireproofColor = ComponentHelper.PURITY_FIRE_COLOR.getRGB();
            }

            int finalFireproofColor = fireproofColor;
            ComponentHelper.setIconsFont(fireproofIcon, Chrysalis.MOD_ID);
            Component fireproofTooltip = ItemHelper.addTooltipWithIcon(fireproofIcon, Component.translatable("gui.chrysalis.item.fireproof").withStyle(style -> style.withItalic(true).withColor(finalFireproofColor)));
            list.add(fireproofTooltip);
        }

        if (itemStack.is(CTags.WAXED_BLOCK_ITEMS)) {
            MutableComponent waxedIcon = ComponentHelper.WAXED_ICON;
            ComponentHelper.setIconsFont(waxedIcon, Chrysalis.MOD_ID);
            Component waxedTooltip = ItemHelper.addTooltipWithIcon(waxedIcon, Component.translatable("gui.chrysalis.item.waxed").withStyle(style -> style.withItalic(true).withColor(ComponentHelper.WAXED_COLOR.getRGB())));
            list.add(waxedTooltip);
        }

        if (itemStack.is(Items.DEBUG_STICK)) {
            ItemHelper.addUseTooltip(list);
            list.add(CommonComponents.space().append(Component.translatable("item.chrysalis.debug_stick.description").withStyle(ChatFormatting.BLUE)));
        }

        if (itemStack.is(Items.LIGHT)) {
            BlockItemStateProperties blockItemStateProperties = itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
            list.add(Component.translatable("item.chrysalis.light.description", blockItemStateProperties.get(LightBlock.LEVEL) != null ? blockItemStateProperties.get(LightBlock.LEVEL) : 15).withStyle(ChatFormatting.GRAY));
        }

        if (itemStack.has(DataComponents.STORED_ENCHANTMENTS)) {

            list.add(CommonComponents.EMPTY);
            list.add(Component.translatable("gui.chrysalis.item.enchantment.applies_to").withStyle(ChatFormatting.GRAY));

            for (Holder<Enchantment> holder : Objects.requireNonNull(itemStack.get(DataComponents.STORED_ENCHANTMENTS)).keySet()) {
                String modId = ResourceLocation.fromNamespaceAndPath(holder.getRegisteredName().split(":")[0], holder.getRegisteredName().split(":")[1]).getNamespace();
                if (holder.value().getSupportedItems().unwrapKey().isEmpty()) return;
                String enchantmentTag = Arrays.toString(holder.value().getSupportedItems().unwrapKey().get().location().toString().split(modId + ":enchantable/")).replace("[, ", "").replace("]", "");
                list.add(CommonComponents.space().append(Component.translatable("tag.item." + modId + ".enchantable." + enchantmentTag).withStyle(ChatFormatting.BLUE)));
            }
        }
    }

    @Mixin(ItemStack.class)
    public static abstract class ItemStackMixin implements DataComponentHolder {

        @Shadow public abstract Item getItem();
        @Shadow public abstract ItemStack copy();

        /**
         * Adds a chrysalis tooltip to any new item registered by the mod.
         **/

        @OnlyIn(Dist.CLIENT)
        @Inject(method = "getTooltipLines", at = @At("TAIL"))
        private void chrysalis$addModNameTooltip(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
            if (!tooltipFlag.isAdvanced() && CConfigOptions.CHRYSALIS_TOOLTIP.get()) ItemHelper.addModNameTooltip(tooltipContext, this.copy(), Chrysalis.MOD_ID, ComponentHelper.CHRYSALIS_ICON, ComponentHelper.CHRYSALIS_COLOR.getRGB(), cir);
        }

        @Inject(method = "getItemName", at = @At("RETURN"), cancellable = true)
        private void chrysalis$getItemNameColor(CallbackInfoReturnable<Component> cir) {
            if (this.has(CDataComponents.NAME_COLOR)) cir.setReturnValue(this.chrysalis$nameColorComponent(cir));
        }

        @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
        private void chrysalis$getDisplayNameColor(CallbackInfoReturnable<Component> cir) {
            if (this.has(CDataComponents.NAME_COLOR)) cir.setReturnValue(this.chrysalis$nameColorComponent(cir));
        }

        @Unique
        private MutableComponent chrysalis$nameColorComponent(CallbackInfoReturnable<Component> cir) {
            return cir.getReturnValue().copy().withColor(Objects.requireNonNull(this.get(CDataComponents.NAME_COLOR.get())));
        }
    }
}