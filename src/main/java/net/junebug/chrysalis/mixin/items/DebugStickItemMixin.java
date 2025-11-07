package net.junebug.chrysalis.mixin.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DebugStickItem.class)
public class DebugStickItemMixin {

    /**
     * Prevents the player's hand from swinging while right-clicking with a debug stick in survival mode.
     **/

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventDebugStickRightClickingInSurvival(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        if (useOnContext.getPlayer() != null && !useOnContext.getPlayer().canUseGameMasterBlocks()) cir.setReturnValue(InteractionResult.PASS);
    }
}