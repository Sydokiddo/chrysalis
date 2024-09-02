package net.sydokiddo.chrysalis.mixin.components;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsMixin {

    @Inject(method = "addToTooltip", at = @At("HEAD"))
    private void chrysalis$changeEnchantmentTooltipLines(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo ci) {
        // TODO do this properly
//        if (ItemHelper.hasArmorTrim(this.copy())) tooltip.add(CommonComponents.EMPTY);
        consumer.accept(Component.translatable("gui.chrysalis.item.enchantments").withStyle(ChatFormatting.GRAY));

//        for (int enchantments = 0; enchantments < listTag.size(); ++enchantments) {
//            CompoundTag compoundTag = listTag.getCompound(enchantments);
//            BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundTag)).ifPresent(enchantment -> tooltip.add(CommonComponents.space().append(enchantment.getFullname(EnchantmentHelper.getEnchantmentLevel(compoundTag)))));
//        }
    }

}
