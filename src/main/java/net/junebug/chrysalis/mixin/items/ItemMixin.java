package net.junebug.chrysalis.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.items.CDataComponents;
import net.junebug.chrysalis.common.misc.CGameRules;
import net.junebug.chrysalis.util.helpers.ComponentHelper;
import net.junebug.chrysalis.util.helpers.ItemHelper;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.util.technical.config.CConfigOptions;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mixin(Item.class)
public class ItemMixin {

    /**
     * Adds various server-side tooltips.
     **/

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void chrysalis$addServerSideTooltips(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo info) {

        if (!CConfigOptions.REWORKED_TOOLTIPS.get()) return;

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

        DamageResistant damageResistant = itemStack.get(DataComponents.DAMAGE_RESISTANT);

        if (damageResistant != null && damageResistant.types() == DamageTypeTags.IS_FIRE) {

            MutableComponent fireproofIcon = ComponentHelper.FLAME_ICON;
            int fireproofColor = ComponentHelper.FIRE_COLOR.getRGB();

            if (ItemHelper.listContainsName(itemStack, ComponentHelper.SOUL_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.SOUL_FLAME_ICON;
                fireproofColor = ComponentHelper.SOUL_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.COPPER_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.COPPER_FLAME_ICON;
                fireproofColor = ComponentHelper.COPPER_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.PRIMORDIAL_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.PRIMORDIAL_FLAME_ICON;
                fireproofColor = ComponentHelper.PRIMORDIAL_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.MEMORY_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.MEMORY_FLAME_ICON;
                fireproofColor = ComponentHelper.MEMORY_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.VOID_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.VOID_FLAME_ICON;
                fireproofColor = ComponentHelper.VOID_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.ANCIENT_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.ANCIENT_FLAME_ICON;
                fireproofColor = ComponentHelper.ANCIENT_FIRE_COLOR.getRGB();
            } else if (ItemHelper.listContainsName(itemStack, ComponentHelper.CORPSE_FIRE_NAMES)) {
                fireproofIcon = ComponentHelper.CORPSE_FLAME_ICON;
                fireproofColor = ComponentHelper.CORPSE_FIRE_COLOR.getRGB();
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
    }

    @Mixin(ItemStack.class)
    public static abstract class ItemStackMixin implements DataComponentHolder {

        @Shadow public abstract Item getItem();
        @Shadow public abstract ItemStack copy();
        @Shadow public abstract boolean isDamaged();
        @Shadow public abstract int getDamageValue();
        @Shadow public abstract int getMaxDamage();
        @Shadow public abstract @NotNull DataComponentMap getComponents();

        /**
         * Adds various client-side tooltips.
         **/

        @SuppressWarnings("deprecation")
        @OnlyIn(Dist.CLIENT)
        @Inject(method = "getTooltipLines", at = @At("TAIL"))
        private void chrysalis$addClientSideTooltips(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {

            if (CConfigOptions.REWORKED_TOOLTIPS.get()) {

                if (this.has(DataComponents.STORED_ENCHANTMENTS)) {

                    cir.getReturnValue().add(CommonComponents.EMPTY);
                    cir.getReturnValue().add(Component.translatable("gui.chrysalis.item.enchantment.applies_to").withStyle(ChatFormatting.GRAY));

                    for (Holder<Enchantment> holder : Objects.requireNonNull(this.get(DataComponents.STORED_ENCHANTMENTS)).keySet()) {
                        String modId = ResourceLocation.fromNamespaceAndPath(holder.getRegisteredName().split(":")[0], holder.getRegisteredName().split(":")[1]).getNamespace();
                        if (holder.value().getSupportedItems().unwrapKey().isEmpty()) return;
                        String enchantmentTag = Arrays.toString(holder.value().getSupportedItems().unwrapKey().get().location().toString().split(modId + ":enchantable/")).replace("[, ", "").replace("]", "");
                        cir.getReturnValue().add(CommonComponents.space().append(Component.translatable("tag.item." + modId + ".enchantable." + enchantmentTag).withStyle(ChatFormatting.BLUE)));
                    }
                }

                if (this.has(DataComponents.REPAIRABLE) && Minecraft.getInstance().screen instanceof AnvilScreen) {

                    cir.getReturnValue().add(CommonComponents.EMPTY);
                    cir.getReturnValue().add(Component.translatable("gui.chrysalis.item.repairable_with").withStyle(ChatFormatting.GRAY));

                    int lines = 0;
                    HolderSet<Item> repairItems = Objects.requireNonNull(this.get(DataComponents.REPAIRABLE)).items();

                    for (Holder<Item> holder : repairItems) {

                        cir.getReturnValue().add(CommonComponents.space().append(holder.value().getName().copy().withStyle(ChatFormatting.BLUE)));
                        ++lines;

                        if (lines >= 3) {
                            int additionalLines = repairItems.size() - 3;
                            if (additionalLines > 0) cir.getReturnValue().add(CommonComponents.space().append(Component.translatable("gui.chrysalis.item.more_items", additionalLines).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
                            break;
                        }
                    }
                }
            }

            EventHooks.onItemTooltip(this.copy(), player, cir.getReturnValue(), tooltipFlag, tooltipContext);

            if (tooltipFlag.isAdvanced()) {
                if (this.isDamaged()) cir.getReturnValue().add(Component.translatable("item.durability", this.getMaxDamage() - this.getDamageValue(), this.getMaxDamage()));
                cir.getReturnValue().add(Component.literal(BuiltInRegistries.ITEM.getKey(this.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
                int componentSize = this.getComponents().size();
                if (componentSize > 0) cir.getReturnValue().add(Component.translatable("item.components", componentSize).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        /**
         * Cancels the default onItemTooltip event hook and advanced tooltip check, as the positioning of them has been moved.
         **/

        @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/event/EventHooks;onItemTooltip(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;Lnet/minecraft/world/item/Item$TooltipContext;)Lnet/neoforged/neoforge/event/entity/player/ItemTooltipEvent;"))
        private ItemTooltipEvent chrysalis$cancelDefaultOnItemTooltipEvent(ItemStack itemStack, @Nullable Player player, List<Component> list, TooltipFlag tooltipFlag, Item.TooltipContext tooltipContext) {
            return null;
        }

        @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/TooltipFlag;isAdvanced()Z"))
        private boolean chrysalis$cancelDefaultAdvancedTooltips(TooltipFlag tooltipFlag) {
            return false;
        }

        /**
         * Allows for items with the name color data component to change the item's name color.
         **/

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

    @SuppressWarnings("unused")
    @Mixin(ItemCooldowns.class)
    public static class ItemCooldownsMixin {

        /**
         * Prevents adding cooldowns to items if the itemCooldowns game rule is set to false.
         **/

        @Inject(method = "addCooldown(Lnet/minecraft/resources/ResourceLocation;I)V", at = @At("HEAD"), cancellable = true)
        private void chrysalis$preventAddingCooldown(ResourceLocation resourceLocation, int cooldown, CallbackInfo info) {
            if (Chrysalis.gameRules != null && !Chrysalis.gameRules.getBoolean(CGameRules.RULE_ITEM_COOLDOWNS)) info.cancel();
        }
    }
}