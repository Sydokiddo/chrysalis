package net.sydokiddo.chrysalis.mixin.util;

import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.Iterator;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    private static Enchantment currentEnchantment;
    private static ItemStack itemStack;

    @SuppressWarnings("ALL")
    @Inject(method = "getAvailableEnchantmentResults", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;isTreasureOnly()Z"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void chrysalis_extractEnchantment(int i, ItemStack stack, boolean bl, CallbackInfoReturnable<List<EnchantmentInstance>> cir, List list, Item item, boolean bl2, Iterator var6, Enchantment enchantment) {
        currentEnchantment = enchantment;
        itemStack = stack;
    }

    @Redirect(method = "getAvailableEnchantmentResults", at =
    @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;canEnchant(Lnet/minecraft/world/item/Item;)Z"))
    private static boolean chrysalis_redirectCanEnchant(EnchantmentCategory instance, Item item) {
        if (itemStack.getItem() instanceof ElytraItem) {
            return currentEnchantment.canEnchant(itemStack);
        } else {
            return instance.canEnchant(item);
        }
    }
}