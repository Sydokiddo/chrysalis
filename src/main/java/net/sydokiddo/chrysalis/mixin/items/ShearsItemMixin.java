package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {

    /**
     * Any blocks in the mineable/shears tag will be mined faster with Shears.
     **/

    // TODO do
//    @Inject(method = "getDestroySpeed", at = @At(value = "RETURN"), cancellable = true)
//    private void chrysalis$getShearsDestroySpeed(ItemStack itemStack, BlockState blockState, CallbackInfoReturnable<Float> cir) {
//        if (blockState.is(ChrysalisTags.MINEABLE_WITH_SHEARS)) cir.setReturnValue(15.0F);
//    }
//
//    @Inject(method = "isCorrectToolForDrops", at = @At(value = "RETURN"), cancellable = true)
//    private void chrysalis$shearsCanMineBlocks(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
//        if (blockState.is(ChrysalisTags.MINEABLE_WITH_SHEARS)) cir.setReturnValue(true);
//    }
}