package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(Boat.class)
public abstract class BoatMixin extends Entity {

    private BoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Any mobs in the cannot_ride_boats tag will never be able to mount boats when near them.
     **/

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;startRiding(Lnet/minecraft/world/entity/Entity;)Z"), cancellable = true)
    private void chrysalis$preventMobsFromRidingBoats(CallbackInfo info) {

        List<Entity> nearbyEntities = this.level().getEntities(this, this.getBoundingBox().inflate(0.2F, -0.01F, 0.2F), EntitySelector.pushableBy(this));

        for (Entity entity : nearbyEntities) {
            if (entity.getType().is(ChrysalisTags.CANNOT_RIDE_BOATS)) {
                info.cancel();
            }
        }
    }
}