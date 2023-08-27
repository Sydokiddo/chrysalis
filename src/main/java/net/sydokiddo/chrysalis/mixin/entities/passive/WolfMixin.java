package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public class WolfMixin {

    @Inject(at = @At("HEAD"), method = "isFood", cancellable = true)
    public void chrysalis_isWolfFood(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {

        Item item = itemStack.getItem();

        if (itemStack.is(ChrysalisTags.MEATS) && item.isEdible()) {
            cir.setReturnValue(true);
        }
    }
}