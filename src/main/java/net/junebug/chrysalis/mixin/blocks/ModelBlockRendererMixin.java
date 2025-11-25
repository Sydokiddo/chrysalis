package net.junebug.chrysalis.mixin.blocks;

import net.junebug.chrysalis.common.blocks.custom_blocks.BarricadeFullBlock;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@OnlyIn(Dist.CLIENT)
@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {

    /**
     * If a barricade block has a disguised block state set, the block state for occlusion will be said to said disguised block state rather than the barricade block's block state.
     **/

    @Redirect(method = "tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;shouldRenderFace(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"))
    private boolean chrysalis$occludeBarricadeBlockFaces(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, BlockState adjacentState, Direction direction) {

        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
        mutableBlockPos.setWithOffset(blockPos, direction);

        if (blockGetter.getBlockState(blockPos).getBlock() instanceof BarricadeFullBlock barricade && barricade.getBarricadeBlockEntity(blockGetter, blockPos).isPresent() && !barricade.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState().isEmpty()) blockState = barricade.getBarricadeBlockEntity(blockGetter, blockPos).get().getDisguisedBlockState();
        if (blockGetter.getBlockState(mutableBlockPos).getBlock() instanceof BarricadeFullBlock barricade && barricade.getBarricadeBlockEntity(blockGetter, mutableBlockPos).isPresent() && !barricade.getBarricadeBlockEntity(blockGetter, mutableBlockPos).get().getDisguisedBlockState().isEmpty()) adjacentState = barricade.getBarricadeBlockEntity(blockGetter, mutableBlockPos).get().getDisguisedBlockState();

        return Block.shouldRenderFace(blockGetter, blockPos, blockState, adjacentState, direction);
    }
}