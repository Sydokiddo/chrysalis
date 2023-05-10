package net.sydokiddo.chrysalis.mixin.entities.vehicles;

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

    public BoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    // Certain mobs are unable to ride Boats

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;startRiding(Lnet/minecraft/world/entity/Entity;)Z"), cancellable = true)
    private void chrysalis_preventMobsFromRidingBoats(CallbackInfo ci) {

        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.20000000298023224, -0.009999999776482582, 0.20000000298023224), EntitySelector.pushableBy(this));

        for (Entity entity : list) {
            if (entity.getType().is(ChrysalisTags.CANNOT_RIDE_BOATS)) {
                ci.cancel();
            }
        }
    }
}