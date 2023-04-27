package net.sydokiddo.chrysalis.mixin.entities.piglin;

import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.*;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {

    // Un-hard-codes Piglin bartering items and makes it into a tag

    @Inject(at = @At("HEAD"), method = "isBarterCurrency", cancellable = true)
    private static void chrysalis_piglinBarteringItemsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(itemStack.is(ChrysalisTags.PIGLIN_BARTERING_ITEMS));
    }
}
