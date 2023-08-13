package net.sydokiddo.chrysalis.mixin.blocks;

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
public class NetherPortalShapeMixin {

    /**
     * Blocks used to create Nether Portal frames are now driven by the nether_portal_base_blocks tag.
     **/

    @Inject(at = @At("HEAD"), method = "method_30487", cancellable = true)
    private static void chrysalis_netherPortalBlocksTag(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(blockState.is(ChrysalisTags.NETHER_PORTAL_BASE_BLOCKS));
    }
}