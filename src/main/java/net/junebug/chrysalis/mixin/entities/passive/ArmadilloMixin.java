package net.junebug.chrysalis.mixin.entities.passive;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Armadillo.class)
public class ArmadilloMixin {

    /**
     * Armadillos are now scared of entities that are fall flying.
     **/

    @Inject(at = @At("HEAD"), method = "isScaredBy", cancellable = true)
    private void chrysalis$makeArmadillosScaredOfElytraGliding(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity.isFallFlying()) cir.setReturnValue(true);
    }
}