package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin extends Entity {

    private ExperienceOrbMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Prevents experience orbs from being picked up by players who are dying.
     **/

    @Inject(at = @At("HEAD"), method = "playerTouch", cancellable = true)
    private void chrysalis$preventExperiencePickingUp(Player player, CallbackInfo info) {
        if (!this.level().isClientSide() && player.isDeadOrDying()) info.cancel();
    }
}