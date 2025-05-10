package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.sydokiddo.chrysalis.common.items.CItems;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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