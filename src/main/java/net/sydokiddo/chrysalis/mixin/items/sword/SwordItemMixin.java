package net.sydokiddo.chrysalis.mixin.items.sword;

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

    // Any blocks in the mined_faster_with_swords tag will be destroyed faster by swords

    @Inject(method = "getDestroySpeed", at = @At(value = "RETURN"), cancellable = true)
    public void chrysalis_getSwordDestroySpeed(ItemStack itemStack, BlockState blockState, CallbackInfoReturnable<Float> cir) {
        if (blockState.is(ChrysalisTags.MINED_FASTER_WITH_SWORDS)) {
            cir.setReturnValue(15.0F);
        }
    }

    @Inject(method = "isCorrectToolForDrops", at = @At(value = "RETURN"), cancellable = true)
    public void chrysalis_swordCanMineBlocks(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.is(ChrysalisTags.MINED_FASTER_WITH_SWORDS)) {
            cir.setReturnValue(true);
        }
    }
}
