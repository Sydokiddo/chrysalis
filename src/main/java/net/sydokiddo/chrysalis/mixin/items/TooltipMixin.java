package net.sydokiddo.chrysalis.mixin.items;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.helpers.ComponentHelper;
import net.sydokiddo.chrysalis.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin({DiscFragmentItem.class, BannerItem.class, CrossbowItem.class, FireworkRocketItem.class, MobBucketItem.class, WrittenBookItem.class, PotionItem.class})
public class TooltipMixin extends Item {

    private TooltipMixin(Properties properties) {
        super(properties);
    }

    /**
     * Allows for the original appendHoverText method to be executed after the overwritten one on various item classes.
     **/

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void chrysalis$allowOriginalTooltips(ItemStack itemStack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo info) {
        super.appendHoverText(itemStack, context, list, tooltipFlag);
    }

    @Mixin(ArmorTrim.class)
    public static abstract class ArmorTrimMixin {

        @Shadow public abstract boolean showInTooltip();
        @Shadow public abstract Holder<TrimPattern> pattern();
        @Shadow public abstract Holder<TrimMaterial> material();

        /**
         * Cleans up the armor trim tooltip layout.
         **/

        @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
        private void chrysalis$changeArmorTrimTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo info) {
            if (this.showInTooltip() && CConfigOptions.REWORKED_TOOLTIPS.get()) {
                info.cancel();
                consumer.accept(Component.translatable("gui.chrysalis.item.armor_trim", this.pattern().value().description().copy().withStyle(ChatFormatting.GRAY), this.material().value().description()).withStyle(this.material().value().description().getStyle()));
            }
        }
    }

    @Mixin(ItemEnchantments.class)
    public static class ItemEnchantmentsMixin {

        @Shadow @Final boolean showInTooltip;
        @Shadow @Final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

        /**
         * Cleans up the enchantment tooltip layout.
         **/

        @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
        private void chrysalis$changeEnchantmentTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo info) {

            if (!CConfigOptions.REWORKED_TOOLTIPS.get()) return;
            info.cancel();

            if (this.showInTooltip) {

                if (!this.enchantments.isEmpty()) {
                    consumer.accept(CommonComponents.EMPTY);
                    consumer.accept(Component.translatable("gui.chrysalis.item.enchantments").withStyle(ChatFormatting.GRAY));
                }

                HolderSet<Enchantment> holderSet = chrysalis$getTagOrEmpty(tooltipContext.registries());

                for (Holder<Enchantment> holder : holderSet) {
                    int enchantmentAmount = this.enchantments.getInt(holder);
                    if (enchantmentAmount > 0) consumer.accept(CommonComponents.space().append(Enchantment.getFullname(holder, enchantmentAmount)));
                }

                for (Object2IntMap.Entry<Holder<Enchantment>> entry : this.enchantments.object2IntEntrySet()) {
                    if (!holderSet.contains(entry.getKey())) consumer.accept(CommonComponents.space().append(Enchantment.getFullname(entry.getKey(), entry.getIntValue())));
                }
            }
        }

        @Unique
        private static HolderSet<Enchantment> chrysalis$getTagOrEmpty(@Nullable HolderLookup.Provider provider) {

            if (provider != null) {
                Optional<HolderSet.Named<Enchantment>> optional = provider.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.TOOLTIP_ORDER);
                if (optional.isPresent()) return optional.get();
            }

            return HolderSet.direct();
        }
    }

    @Mixin(Enchantment.class)
    public static class EnchantmentMixin {

        /**
         * Changes the color of non-curse enchantment tooltips to a purple color, and adds an experience icon next to max-level enchantments.
         **/

        @Inject(method = "getFullname", at = @At("HEAD"), cancellable = true)
        private static void chrysalis$changeEnchantmentNameTooltip(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<Component> cir) {
            if (CConfigOptions.REWORKED_TOOLTIPS.get()) {

                int color;
                int darkerColor;

                if (enchantment.is(EnchantmentTags.CURSE)) {
                    color = ComponentHelper.CURSE_COLOR.getRGB();
                    darkerColor = ComponentHelper.CURSE_DARKER_COLOR.getRGB();
                } else {
                    color = ComponentHelper.ENCHANTMENT_COLOR.getRGB();
                    darkerColor = ComponentHelper.ENCHANTMENT_DARKER_COLOR.getRGB();
                }

                MutableComponent warningIcon = ComponentHelper.WARNING_ICON;
                ComponentHelper.setIconsFont(warningIcon, Chrysalis.MOD_ID);

                MutableComponent defaultComponent = enchantment.value().description().copy().withColor(color);
                MutableComponent mainComponent;

                if (level > enchantment.value().getMaxLevel()) mainComponent = (MutableComponent) ItemHelper.addTooltipWithIcon(warningIcon, defaultComponent);
                else mainComponent = defaultComponent;

                if (level != 1 || enchantment.value().getMaxLevel() != 1) mainComponent.append(CommonComponents.space()).append(Component.translatable("gui.chrysalis.item.enchantment.level",
                chrysalis$enchantmentLevelComponent(level).copy().withColor(color), Component.translatable("gui.chrysalis.item.enchantment.max_level", chrysalis$enchantmentLevelComponent(enchantment.value().getMaxLevel())).copy()
                .withStyle(mainComponent.getStyle().withFont(ComponentHelper.FIVE_LOWERED_FONT).withColor(darkerColor)).copy()));

                cir.setReturnValue(mainComponent);
            }
        }

        @Unique
        private static Component chrysalis$enchantmentLevelComponent(int level) {
            return Component.translatable("enchantment.level." + level);
        }
    }
}