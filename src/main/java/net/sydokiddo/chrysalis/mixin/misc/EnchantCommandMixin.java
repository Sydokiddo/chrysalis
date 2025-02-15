package net.sydokiddo.chrysalis.mixin.misc;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Collection;
import java.util.Objects;

@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {

    /**
     * Allows for the /enchant command to be able to enchant any item, even if said enchantment cannot normally be applied in survival mode.
     **/

    @ModifyExpressionValue(method = "enchant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;canEnchant(Lnet/minecraft/world/item/ItemStack;)Z"))
    private static boolean chrysalis$allowAllEnchantments(boolean original) {
        return true;
    }

    @Redirect(method = "enchant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;isEnchantmentCompatible(Ljava/util/Collection;Lnet/minecraft/core/Holder;)Z"))
    private static boolean chrysalis$makeEnchantmentCompatible(Collection<Holder<Enchantment>> collection, Holder<Enchantment> holder) {
        return true;
    }

    @Redirect(method = "enchant", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I"))
    private static int chrysalis$removeEnchantmentLimit(Enchantment enchantment) {
        return 255;
    }

    /**
     * Prevents enchanting if the inputted enchantment value is lower than or equal to the enchantment value that is already on the item.
     **/

    @Inject(method = "enchant", at = @At(value = "HEAD"), cancellable = true)
    private static void chrysalis$preventEnchantingIfValueIsTooLow(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, Holder<Enchantment> holder, int enchantmentLevel, CallbackInfoReturnable<Integer> cir) {
        for (Entity entity : collection) {
            if (entity instanceof LivingEntity livingEntity && enchantmentLevel <= EnchantmentHelper.getItemEnchantmentLevel(holder, livingEntity.getMainHandItem())) {
                Objects.requireNonNull(commandSourceStack.getPlayer()).sendSystemMessage(Component.translatable("gui.chrysalis.commands.enchant.fail.low_level").withStyle(ChatFormatting.RED));
                cir.setReturnValue(0);
            }
        }
    }
}