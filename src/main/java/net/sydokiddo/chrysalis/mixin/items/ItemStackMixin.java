package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();
    @Shadow protected abstract int getHideFlags();
    @Shadow public abstract boolean isEnchanted();
    @Shadow public abstract ListTag getEnchantmentTags();
    @Shadow public abstract ItemStack copy();

    @Redirect(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;appendEnchantmentNames(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"))
    private void chrysalis$changeEnchantmentTooltipLines(List<Component> tooltip, ListTag listTag) {

        if (this.isEnchanted() && this.getEnchantmentTags() != null && (this.getHideFlags() & ItemStack.TooltipPart.ENCHANTMENTS.getMask()) == 0) {
            if (ItemHelper.hasArmorTrim(this.copy())) tooltip.add(CommonComponents.EMPTY);
            tooltip.add(Component.translatable("gui.chrysalis.item.enchantments").withStyle(ChatFormatting.GRAY));
        }

        for (int enchantments = 0; enchantments < listTag.size(); ++enchantments) {
            CompoundTag compoundTag = listTag.getCompound(enchantments);
            BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundTag)).ifPresent(enchantment -> tooltip.add(CommonComponents.space().append(enchantment.getFullname(EnchantmentHelper.getEnchantmentLevel(compoundTag)))));
        }
    }
}