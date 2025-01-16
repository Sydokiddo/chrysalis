package net.sydokiddo.chrysalis.mixin.items;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin({DiscFragmentItem.class, BannerItem.class, ShieldItem.class, CrossbowItem.class, FireworkRocketItem.class, MobBucketItem.class, WrittenBookItem.class, PotionItem.class})
public class TooltipMixin extends Item {

    private TooltipMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void chrysalis$allowOriginalTooltips(ItemStack itemStack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo info) {
        super.appendHoverText(itemStack, context, list, tooltipFlag);
    }

    @SuppressWarnings("unused")
    @Mixin(ArmorTrim.class)
    public static abstract class ArmorTrimMixin {

        @Shadow public abstract boolean showInTooltip();
        @Shadow public abstract Holder<TrimPattern> pattern();
        @Shadow public abstract Holder<TrimMaterial> material();

        @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
        private void chrysalis$changeArmorTrimTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo info) {
            if (this.showInTooltip()) {
                info.cancel();
                consumer.accept(Component.translatable("gui.chrysalis.item.armor_trim", this.pattern().value().description().copy().withStyle(ChatFormatting.GRAY), this.material().value().description()).withStyle(this.material().value().description().getStyle()));
            }
        }
    }

    @SuppressWarnings("unused")
    @Mixin(ItemEnchantments.class)
    public static abstract class ItemEnchantmentsMixin {

        @Shadow @Final boolean showInTooltip;
        @Shadow @Final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

        @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
        private void chrysalis$changeEnchantmentTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo info) {

            info.cancel();

            if (this.showInTooltip) {

                if (!this.enchantments.isEmpty()) {
                    consumer.accept(CommonComponents.EMPTY);
                    consumer.accept(Component.translatable("gui.chrysalis.item.enchantments").withStyle(ChatFormatting.GRAY));
                }

                HolderSet<Enchantment> holderSet = getTagOrEmpty(tooltipContext.registries());

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
        private static HolderSet<Enchantment> getTagOrEmpty(@Nullable HolderLookup.Provider provider) {

            if (provider != null) {
                Optional<HolderSet.Named<Enchantment>> optional = provider.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.TOOLTIP_ORDER);
                if (optional.isPresent()) return optional.get();
            }

            return HolderSet.direct();
        }
    }

    @SuppressWarnings("unused")
    @Mixin(Enchantment.class)
    public static class EnchantmentMixin {

        @Redirect(method = "getFullname", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Style;withColor(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/Style;", ordinal = 1))
        private static Style chrysalis$changeEnchantmentTooltipColor(Style style, ChatFormatting chatFormatting) {
            return Style.EMPTY.withColor(ChatFormatting.BLUE);
        }
    }
}