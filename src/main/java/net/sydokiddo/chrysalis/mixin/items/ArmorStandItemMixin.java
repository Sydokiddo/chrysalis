package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.context.UseOnContext;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventArmorStandPlacement(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = useOnContext.getPlayer();
        if (player != null && player.hasEffect(Holder.direct(ChrysalisEffects.BUILDING_FATIGUE.get())) && !player.getAbilities().instabuild) cir.setReturnValue(InteractionResult.FAIL);
    }
}