package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.extensions.IBlockStateExtension;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.sydokiddo.chrysalis.util.sounds.codecs.BlockSoundData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Objects;
import java.util.Optional;

@Mixin(BlockBehaviour.class)
public class BlockMixin {

    /**
     * Pulls information from the data-driven block sound group system and applies it as the block's sound group.
     **/

    @Inject(at = @At("HEAD"), method = "getSoundType", cancellable = true)
    private void chrysalis$blockSoundData(BlockState blockState, CallbackInfoReturnable<SoundType> cir) {

        if (ChrysalisMod.registryAccess == null) return;
        Optional<BlockSoundData> optional = ChrysalisMod.registryAccess.lookupOrThrow(ChrysalisRegistry.BLOCK_SOUND_DATA).stream().filter(codec -> codec.blocks().contains(blockState.getBlockHolder())).findFirst();

        if (optional.isPresent()) {
            if (optional.get().forTesting() && !ChrysalisMod.IS_DEBUG) return;
            cir.setReturnValue(optional.get().toSoundType());
        }
    }

    @Mixin(BlockBehaviour.BlockStateBase.class)
    public static abstract class BlockStateBaseMixin {

        @Shadow protected abstract BlockState asState();

        /**
         * Pulls the specified note block instrument from the json file and applies it as the block's note block instrument.
         **/

        @Inject(at = @At("HEAD"), method = "instrument", cancellable = true)
        private void chrysalis$blockNoteBlockInstrumentData(CallbackInfoReturnable<NoteBlockInstrument> cir) {

            if (ChrysalisMod.registryAccess == null) return;
            Optional<BlockSoundData> optional = ChrysalisMod.registryAccess.lookupOrThrow(ChrysalisRegistry.BLOCK_SOUND_DATA).stream().filter(codec -> codec.blocks().contains(this.asState().getBlockHolder())).findFirst();

            if (optional.isPresent()) {
                if (optional.get().forTesting() && !ChrysalisMod.IS_DEBUG || Objects.equals(optional.get().noteBlockInstrument(), "null")) return;
                cir.setReturnValue(BlockSoundData.getNoteBlockInstrument(optional.get().noteBlockInstrument()));
            }
        }

        /**
         * Pulls information from the data-driven block properties system and applies it as the block's properties.
         **/

        @Inject(at = @At("HEAD"), method = "getDestroySpeed", cancellable = true)
        private void chrysalis$blockDestroyTimeData(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {

            if (ChrysalisMod.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());

            if (optional.isPresent()) {
                if (optional.get().forTesting() && !ChrysalisMod.IS_DEBUG) return;
                cir.setReturnValue(optional.get().destroyTime());
            }
        }

        @Inject(at = @At("HEAD"), method = "requiresCorrectToolForDrops", cancellable = true)
        private void chrysalis$blockRequiresToolData(CallbackInfoReturnable<Boolean> cir) {

            if (ChrysalisMod.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());

            if (optional.isPresent()) {
                if (optional.get().forTesting() && !ChrysalisMod.IS_DEBUG) return;
                cir.setReturnValue(optional.get().requiresTool());
            }
        }

        @Inject(at = @At("HEAD"), method = "getLightEmission", cancellable = true)
        private void chrysalis$blockLightLevelData(CallbackInfoReturnable<Integer> cir) {

            if (ChrysalisMod.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());

            if (optional.isPresent()) {
                if (optional.get().forTesting() && !ChrysalisMod.IS_DEBUG) return;
                cir.setReturnValue(optional.get().lightLevel());
            }
        }

        @Inject(at = @At("HEAD"), method = "emissiveRendering", cancellable = true)
        private void chrysalis$blockEmissiveRenderingDate(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {

            if (ChrysalisMod.registryAccess == null) return;
            BlockState blockState = blockGetter.getBlockState(blockPos);
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(blockState);

            if (optional.isPresent()) {
                if (optional.get().forTesting() && !ChrysalisMod.IS_DEBUG) return;
                cir.setReturnValue(optional.get().emissiveRendering());
            }
        }

        @Inject(at = @At("HEAD"), method = "canBeReplaced()Z", cancellable = true)
        private void chrysalis$blockReplaceableData(CallbackInfoReturnable<Boolean> cir) {

            if (ChrysalisMod.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());

            if (optional.isPresent()) {
                if (optional.get().forTesting() && !ChrysalisMod.IS_DEBUG) return;
                cir.setReturnValue(optional.get().replaceable());
            }
        }

        @Inject(at = @At("HEAD"), method = "ignitedByLava", cancellable = true)
        private void chrysalis$blockIgnitedByLavaData(CallbackInfoReturnable<Boolean> cir) {

            if (ChrysalisMod.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());

            if (optional.isPresent()) {
                if (optional.get().forTesting() && !ChrysalisMod.IS_DEBUG) return;
                cir.setReturnValue(optional.get().ignitedByLava());
            }
        }

        @Inject(at = @At("HEAD"), method = "shouldSpawnTerrainParticles", cancellable = true)
        private void chrysalis$blockSpawnsTerrainParticlesData(CallbackInfoReturnable<Boolean> cir) {

            if (ChrysalisMod.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());

            if (optional.isPresent()) {
                if (optional.get().forTesting() && !ChrysalisMod.IS_DEBUG) return;
                cir.setReturnValue(optional.get().spawnsTerrainParticles());
            }
        }

        @Unique
        private Optional<BlockPropertyData> chrysalis$getBlockPropertyData(BlockState blockState) {
            return ChrysalisMod.registryAccess.lookupOrThrow(ChrysalisRegistry.BLOCK_PROPERTY_DATA).stream().filter(codec -> codec.blocks().contains(blockState.getBlockHolder())).findFirst();
        }

        /**
         * Fixes fluid hitboxes for any entity that is able to walk on fluids.
         **/

        @Unique private static final VoxelShape[] chrysalis$voxelShapes;

        static {
            chrysalis$voxelShapes = new VoxelShape[16];
            for (int fluidAmount = 0; fluidAmount < 16; fluidAmount++) {
                chrysalis$voxelShapes[fluidAmount] = Block.box(0.0, 0.0, 0.0, 16.0, fluidAmount, 16.0);
            }
        }

        @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"), cancellable = true)
        private void chrysalis$fixFluidHitboxes(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {
            FluidState fluidState = blockGetter.getFluidState(blockPos);
            int amount = fluidState.getAmount();
            if (amount == 0) return;
            if (collisionContext.isAbove(chrysalis$voxelShapes[amount - 1], blockPos, true) && collisionContext.canStandOnFluid(blockGetter.getFluidState(blockPos.above()), fluidState)) cir.setReturnValue(Shapes.or(cir.getReturnValue(), chrysalis$voxelShapes[amount]));
        }
    }

    @Mixin(Blocks.class)
    public static class BlocksRegistryMixin {

        /**
         * Allows for any mobs in the can_spawn_on_leaves tag to be able to spawn on leaves, instead of just the previously hard-coded ocelots or parrots.
         **/

        @Inject(at = @At("HEAD"), method = "ocelotOrParrot", cancellable = true)
        private static void chrysalis$canSpawnOnLeavesEntityTag(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType, CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(entityType.is(ChrysalisTags.CAN_SPAWN_ON_LEAVES));
        }
    }

    @Mixin(IBlockStateExtension.class)
    public static class IBlockStateExtensionMixin {

        /**
         * Blocks used to create nether portal frames are now driven by the nether_portal_base_blocks tag.
         **/

        @Inject(at = @At("HEAD"), method = "isPortalFrame", cancellable = true)
        private void chrysalis$netherPortalBlocksTag(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
            if (blockGetter.getBlockState(blockPos).is(ChrysalisTags.NETHER_PORTAL_BASE_BLOCKS)) cir.setReturnValue(true);
        }
    }
}