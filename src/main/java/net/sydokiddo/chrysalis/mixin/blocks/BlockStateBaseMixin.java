package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public class BlockStateBaseMixin {

    @Unique private static final VoxelShape[] voxelShapes;

    static {
        voxelShapes = new VoxelShape[16];
        for (int fluidAmount = 0; fluidAmount < 16; fluidAmount++) {
            voxelShapes[fluidAmount] = Block.box(0.0, 0.0, 0.0, 16.0, fluidAmount, 16.0);
        }
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"), cancellable = true)
    private void chrysalis$fixFluidHitboxes(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {

        FluidState fluidState = blockGetter.getFluidState(blockPos);
        int amount = fluidState.getAmount();

        if (amount == 0) return;

        if (collisionContext.isAbove(voxelShapes[amount - 1], blockPos, true) && collisionContext.canStandOnFluid(blockGetter.getFluidState(blockPos.above()), fluidState)) {
            cir.setReturnValue(Shapes.or(cir.getReturnValue(), voxelShapes[amount]));
        }
    }
}