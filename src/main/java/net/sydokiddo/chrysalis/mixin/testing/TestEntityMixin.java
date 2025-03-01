package net.sydokiddo.chrysalis.mixin.testing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityAccess;
import net.sydokiddo.chrysalis.registry.ChrysalisTesting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class TestEntityMixin implements EntityAccess {

    @Unique Entity chrysalis$entity = (Entity) (Object) this;
    @Shadow public abstract Level level();

    @Inject(at = @At("TAIL"), method = "walkingStepSound")
    private void chrysalis$emitPulsationParticleFromWalking(BlockPos blockPos, BlockState blockState, CallbackInfo info) {
        this.chrysalis$emitPulsationParticle(false);
    }

    @Inject(at = @At("TAIL"), method = "playMuffledStepSound")
    private void chrysalis$emitPulsationParticleFromMuffledWalking(BlockState blockState, BlockPos blockPos, CallbackInfo info) {
        this.chrysalis$emitPulsationParticle(true);
    }

    @Unique
    private void chrysalis$emitPulsationParticle(boolean muffled) {
        ChrysalisTesting.emitPulsationParticle(this.level(), this.chrysalis$entity, Direction.UP, 0, muffled);
    }
}