package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EyeOfEnderItemMixin {

    /**
     * Prevents End Portals from activating if the doEndPortalActivating gamerule is set to false.
     **/

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/EndPortalFrameBlock;getOrCreatePortalShape()Lnet/minecraft/world/level/block/state/pattern/BlockPattern;"), cancellable = true)
    private void chrysalis$preventEndPortalActivating(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        if (!useOnContext.getLevel().getGameRules().getBoolean(ChrysalisRegistry.RULE_DO_END_PORTAL_ACTIVATING)) {
            cir.cancel();
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}