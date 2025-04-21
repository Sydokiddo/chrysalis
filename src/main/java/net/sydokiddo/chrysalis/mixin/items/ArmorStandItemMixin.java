package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.context.UseOnContext;
import net.sydokiddo.chrysalis.util.helpers.EntityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {

    /**
     * Prevents players from placing armor stands while under the building fatigue effect in survival mode.
     **/

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventArmorStandPlacement(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = useOnContext.getPlayer();
        if (player != null && EntityHelper.hasBuildPreventingEffect(player) && !player.getAbilities().instabuild) cir.setReturnValue(InteractionResult.FAIL);
    }
}