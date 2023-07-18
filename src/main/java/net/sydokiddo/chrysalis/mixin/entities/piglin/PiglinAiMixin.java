package net.sydokiddo.chrysalis.mixin.entities.piglin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.*;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {

    /**
     * Items that can be bartered to Piglins is now determined by the piglin_bartering_items tag.
     **/

    @Inject(at = @At("HEAD"), method = "isBarterCurrency", cancellable = true)
    private static void chrysalis_piglinBarteringItemsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(itemStack.is(ChrysalisTags.PIGLIN_BARTERING_ITEMS));
    }

    /**
     * Armor that makes Piglins neutral is now determined by the piglin_pacifying_armor tag.
     **/

    @Inject(at = @At("RETURN"), method = "isWearingGold", cancellable = true)
    private static void chrysalis_isValidPiglinPacifyingArmor(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        Iterable<ItemStack> iterable = livingEntity.getArmorSlots();
        for (ItemStack itemStack : iterable) {
            if (itemStack.is(ChrysalisTags.PIGLIN_PACIFYING_ARMOR)) {
                cir.setReturnValue(true);
            }
        }
    }
}
