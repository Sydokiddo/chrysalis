package net.sydokiddo.chrysalis.mixin.blocks;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.common.items.CItems;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public class PortalBlockMixin extends Block {

    @Shadow @Final public static EnumProperty<Direction.Axis> AXIS;

    private PortalBlockMixin(Properties properties) {
        super(properties);
    }

    /**
     * Allows for nether portals to be placed directionally with the nether portal block item.
     **/

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        if (blockPlaceContext.getHorizontalDirection().getAxis() == Direction.Axis.X) return this.defaultBlockState().setValue(AXIS, Direction.Axis.Z);
        else return super.getStateForPlacement(blockPlaceContext);
    }

    /**
     * Middle-clicking on a nether portal gives the nether portal block item.
     **/

    @SuppressWarnings("deprecation")
    @Override
    protected @NotNull ItemStack getCloneItemStack(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean includeData) {
        return CItems.NETHER_PORTAL.toStack();
    }

    /**
     * Nether portals will occlude their faces while adjacent to other nether portals.
     **/

    @Override
    protected boolean skipRendering(@NotNull BlockState blockState, @NotNull BlockState adjacentState, @NotNull Direction direction) {
        if (adjacentState.is(this) && blockState.getValue(AXIS) == adjacentState.getValue(AXIS)) return true;
        return super.skipRendering(blockState, adjacentState, direction);
    }

    @SuppressWarnings("unused")
    @Mixin(EndPortalBlock.class)
    private static abstract class EndPortalBlockMixin extends BaseEntityBlock {

        private EndPortalBlockMixin(Properties properties) {
            super(properties);
        }

        /**
         * Middle-clicking on an end portal gives the end portal block item.
         **/

        @SuppressWarnings("deprecation")
        @Override
        protected @NotNull ItemStack getCloneItemStack(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean includeData) {
            return CItems.END_PORTAL.toStack();
        }
    }

    @Mixin(TheEndPortalBlockEntity.class)
    public static class EndPortalBlockEntityMixin extends BlockEntity {

        @Unique private TheEndPortalBlockEntity chrysalis$endPortalBlockEntity = (TheEndPortalBlockEntity) (Object) this;

        private EndPortalBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
            super(blockEntityType, blockPos, blockState);
        }

        /**
         * Allows for all faces of the end portal's hitbox to render as long as they aren't adjacent to another end portal block.
         **/

        @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
        private void chrysalis$renderAllEndPortalFaces(Direction direction, CallbackInfoReturnable<Boolean> cir) {
            if (this.getLevel() != null && direction.getAxis().isHorizontal()) cir.setReturnValue(!this.getLevel().getBlockState(this.getBlockPos().relative(direction)).is(this.chrysalis$endPortalBlockEntity.getBlockState().getBlock()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Mixin(TheEndPortalRenderer.class)
    public static abstract class EndPortalRendererMixin<T extends TheEndPortalBlockEntity> implements BlockEntityRenderer<T> {

        @Shadow protected abstract void renderFace(TheEndPortalBlockEntity blockEntity, Matrix4f pose, VertexConsumer consumer, float x0, float x1, float y0, float y1, float z0, float z1, float z2, float z3, Direction direction);
        @Shadow protected abstract float getOffsetDown();
        @Shadow protected abstract float getOffsetUp();

        /**
         * Adjusts the proportions of the end portal face rendering to match the hitbox.
         **/

        @Inject(method = "renderCube", at = @At("HEAD"), cancellable = true)
        private void chrysalis$changeEndPortalFaceRendering(TheEndPortalBlockEntity blockEntity, Matrix4f pose, VertexConsumer consumer, CallbackInfo info) {

            info.cancel();

            float offsetDown = this.getOffsetDown();
            float offsetUp = this.getOffsetUp();

            this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, offsetDown, offsetUp, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
            this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, offsetUp, offsetDown, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
            this.renderFace(blockEntity, pose, consumer, 1.0F, 1.0F, offsetUp, offsetDown, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
            this.renderFace(blockEntity, pose, consumer, 0.0F, 0.0F, offsetDown, offsetUp, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
            this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, offsetDown, offsetDown, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
            this.renderFace(blockEntity, pose, consumer, 0.0F, 1.0F, offsetUp, offsetUp, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
        }

        /**
         * End portals can now render from up to 256 blocks away, the same as end gateways.
         **/

        @Override
        public int getViewDistance() {
            return 256;
        }
    }

    @SuppressWarnings("unused")
    @Mixin(EndGatewayBlock.class)
    private static abstract class EndGatewayBlockMixin extends BaseEntityBlock {

        private EndGatewayBlockMixin(Properties properties) {
            super(properties);
        }

        /**
         * Middle-clicking on an end gateway gives the end gateway block item.
         **/

        @SuppressWarnings("deprecation")
        @Override
        protected @NotNull ItemStack getCloneItemStack(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState, boolean includeData) {
            return CItems.END_GATEWAY.toStack();
        }
    }
}