package net.sydokiddo.chrysalis.registry.entities.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.animal.FlyingAnimal;

@SuppressWarnings("ALL")
public class HybridFlyingWalkingMoveControl extends MoveControl {

    private final int maxTurn;
    private final boolean hoversInPlace;

    private HybridFlyingWalkingMoveControl(Mob mob, int i, boolean bl) {
        super(mob);
        this.maxTurn = i;
        this.hoversInPlace = bl;
    }

    public void tick() {
        if (this instanceof FlyingAnimal flyingAnimal && flyingAnimal.isFlying()) {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;
                this.mob.setNoGravity(true);
                double d = this.wantedX - this.mob.getX();
                double e = this.wantedY - this.mob.getY();
                double f = this.wantedZ - this.mob.getZ();
                double g = d * d + e * e + f * f;
                if (g < 2.500000277905201E-7) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }
                float h = (float)(Mth.atan2(f, d) * 57.2957763671875) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), h, 90.0F));
                float i;
                if (this.mob.onGround()) {
                    i = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                } else {
                    i = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
                }
                this.mob.setSpeed(i);
                double j = Math.sqrt(d * d + f * f);
                if (Math.abs(e) > 9.999999747378752E-6 || Math.abs(j) > 9.999999747378752E-6) {
                    float k = (float)(-(Mth.atan2(e, j) * 57.2957763671875));
                    this.mob.setXRot(this.rotlerp(this.mob.getXRot(), k, (float)this.maxTurn));
                    this.mob.setYya(e > 0.0 ? i : -i);
                }
            } else {
                if (!this.hoversInPlace) {
                    this.mob.setNoGravity(false);
                }
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
            }
        } else {
            this.mob.setNoGravity(false);
            super.tick();
        }
    }
}