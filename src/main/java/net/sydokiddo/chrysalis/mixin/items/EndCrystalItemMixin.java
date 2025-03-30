package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.common.misc.CTags;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin {

    /**
     * End crystals are now placeable on any blocks in the end_crystal_base_blocks tag.
     **/

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean chrysalis$changeEndCrystalPlacing(BlockState blockState, Block block){
        return blockState.is(CTags.END_CRYSTAL_BASE_BLOCKS);
    }

    /**
     * Prevents players from placing end crystals while under the building fatigue effect in survival mode.
     **/

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventEndCrystalPlacement(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = useOnContext.getPlayer();
        if (player != null && EntityDataHelper.hasBuildPreventingEffect(player) && !player.getAbilities().instabuild) cir.setReturnValue(InteractionResult.FAIL);
    }
}