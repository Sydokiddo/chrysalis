package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherWartBlock.class)
public class NetherWartBlockMixin {

    /**
     * Nether Warts are now placeable on any blocks in the nether_wart_can_grow_on tag.
     **/

    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void chrysalis$netherWartCanGrowOnTag(BlockState floor, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(floor.is(ChrysalisTags.NETHER_WART_CAN_GROW_ON));
    }
}