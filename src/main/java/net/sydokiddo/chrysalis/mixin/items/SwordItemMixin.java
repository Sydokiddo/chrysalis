package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwordItem.class)
public class SwordItemMixin {

    /**
     * Any blocks in the mineable/sword tag will be mined faster with Swords.
     **/

    // TODO do
//    @Inject(method = "getDestroySpeed", at = @At(value = "RETURN"), cancellable = true)
//    private void chrysalis$getSwordDestroySpeed(ItemStack itemStack, BlockState blockState, CallbackInfoReturnable<Float> cir) {
//        if (blockState.is(ChrysalisTags.MINEABLE_WITH_SWORD)) cir.setReturnValue(15.0F);
//    }
//
//    @Inject(method = "isCorrectToolForDrops", at = @At(value = "RETURN"), cancellable = true)
//    private void chrysalis$swordCanMineBlocks(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
//        if (blockState.is(ChrysalisTags.MINEABLE_WITH_SWORD)) cir.setReturnValue(true);
//    }
}