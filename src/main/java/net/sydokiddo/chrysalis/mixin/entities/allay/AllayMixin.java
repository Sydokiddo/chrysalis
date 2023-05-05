package net.sydokiddo.chrysalis.mixin.entities.allay;

import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Allay.class)
public abstract class AllayMixin {

    // Un-hard-codes the items required to duplicate Allays and makes it into a tag

    @Inject(at = @At("HEAD"), method = "isDuplicationItem", cancellable = true)
    private void chrysalis_allayDuplicationItemsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(itemStack.is(ChrysalisTags.DUPLICATES_ALLAYS));
    }
}
