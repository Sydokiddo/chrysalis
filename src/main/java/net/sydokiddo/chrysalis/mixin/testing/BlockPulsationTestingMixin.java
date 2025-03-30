package net.sydokiddo.chrysalis.mixin.testing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.chrysalis.testing.CTesting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockPulsationTestingMixin {

    @Inject(method = "onProjectileHit", at = @At("HEAD"))
    private void chrysalis$emitPulsationParticleFromProjectiles(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile, CallbackInfo info) {
        CTesting.emitPulsationParticle(level, projectile, blockHitResult.getDirection(), 1, projectile.isUnderWater() || blockHitResult.getDirection() == Direction.UP && projectile.getBlockStateOn().is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS));
    }

    @Mixin(Block.class)
    public static class BlockMixin {

        @Inject(method = "fallOn", at = @At("HEAD"))
        public void chrysalis$emitPulsationParticleFromFalling(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float fallDistance, CallbackInfo info) {
            CTesting.emitPulsationParticle(level, entity, Direction.UP, 2, entity.isUnderWater() || entity.getBlockStateOn().is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS));
        }
    }
}