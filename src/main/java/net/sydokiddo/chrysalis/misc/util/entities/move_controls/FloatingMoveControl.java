package net.sydokiddo.chrysalis.misc.util.entities.move_controls;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
public class FloatingMoveControl extends MoveControl {

    /**
     * This move control allows for mobs that integrate it to float around similarly to Ghasts.
     **/

    public final Mob mob;
    public int floatDuration;

    public FloatingMoveControl(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void tick() {
        if (this.operation == MoveControl.Operation.MOVE_TO && this.floatDuration-- <= 0) {

            this.floatDuration = this.floatDuration + this.mob.getRandom().nextInt(5) + 2;
            Vec3 vec3 = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
            vec3 = vec3.normalize();

            if (this.canReach(vec3, Mth.ceil(vec3.length()))) this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(vec3.scale(0.1D)));
            else this.operation = MoveControl.Operation.WAIT;
        }
    }

    public boolean canReach(Vec3 vec3, int length) {

        AABB boundingBox = this.mob.getBoundingBox();

        for (int area = 1; area < length; area++) {
            boundingBox = boundingBox.move(vec3);
            if (!this.mob.level().noCollision(this.mob, boundingBox)) return false;
        }

        return true;
    }
}