package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    /**
     * Prevents players from placing blocks (except for those in the allows_placement_with_building_fatigue tag) while under the building fatigue effect in survival mode.
     **/

    @Inject(method = "canPlace", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventPlayerBlockPlacement(BlockPlaceContext blockPlaceContext, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        Player player = blockPlaceContext.getPlayer();
        if (player != null && player.hasEffect(Holder.direct(ChrysalisEffects.BUILDING_FATIGUE.get())) && !player.getAbilities().instabuild && !blockState.is(ChrysalisTags.ALLOWS_PLACEMENT_WITH_BUILDING_FATIGUE)) cir.setReturnValue(false);
    }
}