package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Chicken.class, Parrot.class})
public class BirdSeedsMixin {

    /**
     * The seed items that Chickens and Parrots can eat is now driven by a tag rather than being a hardcoded list.
     **/

    @Inject(at = @At("HEAD"), method = "isFood", cancellable = true)
    private void chrysalis$seedItemsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(itemStack.is(ChrysalisTags.SEEDS));
    }
}