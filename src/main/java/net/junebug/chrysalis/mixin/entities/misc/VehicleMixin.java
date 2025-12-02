package net.junebug.chrysalis.mixin.entities.misc;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.level.Level;
import net.junebug.chrysalis.common.misc.CTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBoat.class)
public abstract class VehicleMixin extends Entity {

    private VehicleMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Any mobs in the can_always_ride_boats tag can always ride boats regardless of their hitbox size, while mobs in the cannot_ride_boats tag can never ride boats.
     **/

    @Inject(method = "hasEnoughSpaceFor", at = @At("HEAD"), cancellable = true)
    private void chrysalis$changeWhichMobsCanRideBoats(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.getType().is(CTags.CAN_ALWAYS_RIDE_BOATS)) cir.setReturnValue(true);
        else if (entity.getType().is(CTags.CANNOT_RIDE_BOATS)) cir.setReturnValue(false);
    }

    @Mixin(OldMinecartBehavior.class)
    public static abstract class OldMinecartBehaviorMixin extends MinecartBehavior {

        private OldMinecartBehaviorMixin(AbstractMinecart abstractMinecart) {
            super(abstractMinecart);
        }

        /**
         * Any mobs in the cannot_ride_minecarts tag will never be able to mount minecarts when colliding with them.
         **/

        @Inject(method = "pushAndPickupEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;startRiding(Lnet/minecraft/world/entity/Entity;)Z"), cancellable = true)
        private void chrysalis$preventMobsFromRidingOldMinecarts(CallbackInfoReturnable<Boolean> cir) {
            for (Entity entity : this.level().getEntities(this.minecart, this.minecart.getBoundingBox().inflate(0.2D, 0.0D, 0.2D), EntitySelector.pushableBy(this.minecart))) {
                if (entity.getType().is(CTags.CANNOT_RIDE_MINECARTS)) {
                    entity.push(this.minecart);
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Mixin(NewMinecartBehavior.class)
    public static abstract class NewMinecartBehaviorMixin extends MinecartBehavior {

        private NewMinecartBehaviorMixin(AbstractMinecart abstractMinecart) {
            super(abstractMinecart);
        }

        /**
         * The same behavior as above, but for minecarts in the minecart changes experimental feature.
         **/

        @Inject(method = "pickupEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;startRiding(Lnet/minecraft/world/entity/Entity;)Z"), cancellable = true)
        private void chrysalis$preventMobsFromRidingNewMinecarts(CallbackInfoReturnable<Boolean> cir) {
            for (Entity entity : this.level().getEntities(this.minecart, this.minecart.getBoundingBox().inflate(0.2D, 0.0D, 0.2D), EntitySelector.pushableBy(this.minecart))) if (entity.getType().is(CTags.CANNOT_RIDE_MINECARTS)) cir.setReturnValue(false);
        }
    }
}