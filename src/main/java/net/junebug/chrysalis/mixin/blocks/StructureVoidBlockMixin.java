package net.junebug.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StructureVoidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.junebug.chrysalis.common.CConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureVoidBlock.class)
public class StructureVoidBlockMixin extends Block {

    private StructureVoidBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * Changes the structure void's visual hitbox to that of a normal-sized block.
     **/

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    private void chrysalis$changeStructureVoidHitbox(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {
        if (CConfig.IMPROVED_STRUCTURE_VOID_RENDERING.get()) cir.setReturnValue(super.getShape(blockState, blockGetter, blockPos, collisionContext));
    }
}