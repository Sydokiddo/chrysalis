package net.sydokiddo.chrysalis.mixin.blocks.nether_portal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalShape.class)
public class PortalShapeMixin {

    // Un-hard-codes Nether Portal base blocks and makes it into a tag

    @Inject(at = @At("HEAD"), method = "method_30487", cancellable = true)
    private static void chrysalis_netherPortalBlocksTag(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(blockState.is(ChrysalisTags.NETHER_PORTAL_BASE_BLOCKS));
    }
}
