package net.sydokiddo.chrysalis.misc.util.entities;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.FlyingAnimal;

@SuppressWarnings("unused")
public class HybridWalkingFlyingMoveControl extends MoveControl {

    /**
     * This move control allows for mobs that integrate it to use both flying and walking pathfinding types
     **/

    public final Mob mob;
    public final int maxTurn;
    public final boolean hoversInPlace;

    public HybridWalkingFlyingMoveControl(Mob mob, int maxTurn, boolean noGravity) {
        super(mob);
        this.mob = mob;
        this.maxTurn = maxTurn;
        this.hoversInPlace = noGravity;
    }

    @Override
    public void tick() {
        if (this.mob instanceof FlyingAnimal flyingAnimal && flyingAnimal.isFlying()) {
            if (this.operation == Operation.MOVE_TO) {

                this.operation = Operation.WAIT;
                this.mob.setNoGravity(true);

                double x = this.wantedX - this.mob.getX();
                double y = this.wantedY - this.mob.getY();
                double z = this.wantedZ - this.mob.getZ();

                double wantedPosMultiplier = x * x + y * y + z * z;

                if (wantedPosMultiplier < 2.500000277905201E-7D) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }

                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), (float) (Mth.atan2(z, x) * 57.2957763671875D) - 90.0F, 90.0F));
                float speedModifier;

                if (this.mob.onGround()) speedModifier = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                else speedModifier = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));

                this.mob.setSpeed(speedModifier);
                double mathSqrt = Math.sqrt(x * x + z * z);

                if (Math.abs(y) > 9.999999747378752E-6D || Math.abs(mathSqrt) > 9.999999747378752E-6D) {
                    this.mob.setXRot(this.rotlerp(this.mob.getXRot(), (float) (-(Mth.atan2(y, mathSqrt) * 57.2957763671875D)), (float) this.maxTurn));
                    this.mob.setYya(y > 0.0D ? speedModifier : -speedModifier);
                }

            } else {
                if (!this.hoversInPlace) this.mob.setNoGravity(false);
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
            }
        } else {
            this.mob.setNoGravity(false);
            super.tick();
        }
    }
}