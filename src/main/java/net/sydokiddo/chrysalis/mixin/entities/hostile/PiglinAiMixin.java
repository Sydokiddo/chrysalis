package net.sydokiddo.chrysalis.mixin.entities.hostile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.common.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {

    /**
     * Items that can be bartered to piglins is now determined by the piglin_bartering_items tag.
     **/

    @Inject(at = @At("HEAD"), method = "isBarterCurrency", cancellable = true)
    private static void chrysalis$piglinBarteringItemsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(ChrysalisTags.PIGLIN_BARTERING_ITEMS)) cir.setReturnValue(true);
    }

    /**
     * Piglins will run away from any entity in the piglin_avoided tag.
     **/

    @Inject(at = @At("HEAD"), method = "isZombified", cancellable = true)
    private static void chrysalis$piglinAvoidedTag(EntityType<?> entityType, CallbackInfoReturnable<Boolean> cir) {
        if (entityType.is(ChrysalisTags.PIGLIN_AVOIDED)) cir.setReturnValue(true);
    }
}