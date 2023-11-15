package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow @Nullable public abstract CompoundTag getTag();
    @Shadow protected abstract int getHideFlags();
    @Shadow public abstract ListTag getEnchantmentTags();
    @Shadow public abstract boolean isEnchanted();

    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;appendEnchantmentNames(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"))
    private void chrysalis$changeEnchantmentTooltipLines(List<Component> tooltip, ListTag listTag) {

        if (this.isEnchanted() && this.getEnchantmentTags() != null && (this.getHideFlags() & ItemStack.TooltipPart.ENCHANTMENTS.getMask()) == 0) {
            if (this.getTag() != null && this.getTag().contains(ArmorTrim.TAG_TRIM_ID)) {
                tooltip.add(CommonComponents.EMPTY);
            }
            tooltip.add(Component.translatable("gui.chrysalis.item.enchantments").withStyle(ChatFormatting.GRAY));
        }

        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundTag)).ifPresent(enchantment -> tooltip.add(CommonComponents.space().append(enchantment.getFullname(EnchantmentHelper.getEnchantmentLevel(compoundTag)))));
        }
    }
}