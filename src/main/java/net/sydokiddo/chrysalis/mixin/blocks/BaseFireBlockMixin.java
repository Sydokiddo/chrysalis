package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {

    /**
     * Prevents nether portals from activating if the doNetherPortalActivating game rule is set to false.
     **/

    @Inject(at = @At("HEAD"), method = "inPortalDimension", cancellable = true)
    private static void chrysalis$preventNetherPortalActivating(Level level, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().getBoolean(ChrysalisGameRules.RULE_DO_NETHER_PORTAL_ACTIVATING)) cir.setReturnValue(false);
    }
}