package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public abstract class RespawnBlockMixin {

    /**
     * Interacting with a Bed while the bedsExplode gamerule is set to false will display an error message instead of exploding.
     **/

    @Inject(at = @At("HEAD"), method = "useWithoutItem", cancellable = true)
    private void chrysalis$interactWithBed(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().getBoolean(ChrysalisRegistry.RULE_BEDS_EXPLODE) && !level.dimensionType().bedWorks()) {
            player.displayClientMessage(Component.translatable("gui.chrysalis.block.bed.invalid_dimension"), true);
            cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
        }
    }

    @SuppressWarnings("unused")
    @Mixin(RespawnAnchorBlock.class)
    public static abstract class RespawnAnchorBlockMixin {

        @Shadow @Final public static IntegerProperty CHARGE;

        /**
         * Items that can charge Respawn Anchors is now driven by the charges_respawn_anchors tag.
         **/

        @Inject(method = "isRespawnFuel", at = @At("HEAD"), cancellable = true)
        private static void chrysalis$chargesRespawnAnchorsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(itemStack.is(ChrysalisTags.CHARGES_RESPAWN_ANCHORS));
        }

        /**
         * Interacting with a Respawn Anchor while the respawnAnchorsExplode gamerule is set to false will display an error message instead of exploding.
         **/

        @Inject(at = @At("HEAD"), method = "useWithoutItem", cancellable = true)
        private void chrysalis$interactWithRespawnAnchor(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
            if (!level.isClientSide() && level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().getBoolean(ChrysalisRegistry.RULE_RESPAWN_ANCHORS_EXPLODE) && blockState.getValue(CHARGE) > 0 && !level.dimensionType().respawnAnchorWorks()) {
                player.displayClientMessage(Component.translatable("gui.chrysalis.block.generic_respawn_block.invalid_dimension"), true);
                cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
            }
        }
    }
}