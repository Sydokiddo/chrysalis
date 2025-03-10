package net.sydokiddo.chrysalis.mixin.entities.hostile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.chrysalis.registry.status_effects.ChrysalisEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/entity/monster/EnderMan$EndermanLeaveBlockGoal", priority = 1500)
public class EndermanLeaveBlockGoalMixin {

    /**
     * Prevents endermen from placing blocks (except for those in the allows_placement_with_building_fatigue tag) while under the building fatigue effect.
     **/

    @Shadow @Final private EnderMan enderman;

    @Inject(at = @At("HEAD"), method = "canPlaceBlock", cancellable = true)
    private void chrysalis$preventEndermanBlockPlacement(Level level, BlockPos blockPos, BlockState blockState, BlockState secondState, BlockState thirdState, BlockPos secondPos, CallbackInfoReturnable<Boolean> cir) {
        BlockState heldBlockState = this.enderman.getCarriedBlock();
        if (this.enderman.hasEffect(Holder.direct(ChrysalisEffects.BUILDING_FATIGUE.get())) && heldBlockState != null && !heldBlockState.is(ChrysalisTags.ALLOWS_PLACEMENT_WITH_BUILDING_FATIGUE)) cir.setReturnValue(false);
    }
}