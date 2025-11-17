package net.junebug.chrysalis.mixin.blocks;

import net.junebug.chrysalis.common.entities.custom_entities.effects.earthquake.Earthquake;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoulSandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmBlock.class)
public class EarthquakeBlockCollisionFixMixin extends Block {

    public EarthquakeBlockCollisionFixMixin(Properties properties) {
        super(properties);
    }

    /**
     * Fixes an issue where earthquakes cannot break crops above farmland blocks due to their collision hitbox.
     **/

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        if (collisionContext instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof Earthquake) return Shapes.block();
        return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @SuppressWarnings("unused")
    @Mixin(SoulSandBlock.class)
    private static class SoulSandCollisionFixMixin {

        /**
         * Fixes an issue where earthquakes cannot break nether warts above soul sand blocks due to their collision hitbox.
         **/

        @Inject(at = @At("HEAD"), method = "getCollisionShape", cancellable = true)
        private void chrysalis$modifySoulSandCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {
            if (collisionContext instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof Earthquake) cir.setReturnValue(Shapes.block());
        }
    }
}