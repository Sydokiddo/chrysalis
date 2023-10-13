package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {

    /**
     * Prevents Nether Portals from activating if the doNetherPortalActivating gamerule is set to false.
     **/

    @Inject(at = @At("HEAD"), method = "inPortalDimension", cancellable = true)
    private static void chrysalis$preventNetherPortalActivating(Level level, CallbackInfoReturnable<Boolean> cir) {
        if (!level.getGameRules().getBoolean(ChrysalisRegistry.RULE_DO_NETHER_PORTAL_ACTIVATING)) {
            cir.setReturnValue(false);
        }
    }
}