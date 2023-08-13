package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RespawnAnchorBlock.class)
public class RespawnAnchorBlockMixin {

    /**
     * Items that can charge Respawn Anchors is now driven by the charges_respawn_anchors tag.
     **/

    @Inject(method = "isRespawnFuel", at = @At("HEAD"), cancellable = true)
    private static void chrysalis_chargesRespawnAnchorsTag(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(stack.is(ChrysalisTags.CHARGES_RESPAWN_ANCHORS));
    }
}