package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    // TODO i dont even know where to start man
//    @Shadow public abstract boolean isCurse();
//
//    @Redirect(method = "getFullname", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;"))
//    private MutableComponent chrysalis$setEnchantmentTextColor(MutableComponent mutableComponent, ChatFormatting chatFormatting) {
//        if (this.isCurse()) {
//            return mutableComponent.withStyle(ChatFormatting.RED);
//        } else {
//            return mutableComponent.withStyle(ChatFormatting.BLUE);
//        }
//    }
}