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
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.chrysalis.util.blocks.codecs.BlockPropertyTransformer;
import net.sydokiddo.chrysalis.util.sounds.codecs.BlockSoundTransformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;

@Mixin(BlockBehaviour.class)
public class BlockMixin {

    /**
     * Pulls information from the data-driven block sound group system and applies it as the block's sound group.
     **/

    @Inject(at = @At("HEAD"), method = "getSoundType", cancellable = true)
    private void chrysalis$blockSoundTransformer(BlockState blockState, CallbackInfoReturnable<SoundType> cir) {
        if (Chrysalis.registryAccess == null) return;
        Optional<BlockSoundTransformer> registry = Chrysalis.registryAccess.lookupOrThrow(ChrysalisRegistry.BLOCK_SOUND_TRANSFORMER).stream().filter(getBlockState -> getBlockState.blocks().contains(blockState.getBlockHolder())).findFirst();
        if (registry.isPresent() && registry.get().blocks().contains(blockState.getBlockHolder())) cir.setReturnValue(registry.get().toSoundType());
    }

    @SuppressWarnings("unused")
    @Mixin(BlockBehaviour.BlockStateBase.class)
    public static abstract class BlockStateBaseMixin {

        @Shadow protected abstract BlockState asState();

        /**
         * Pulls the specified note block instrument from the json file and applies it as the block's note block instrument.
         **/

        @Inject(at = @At("HEAD"), method = "instrument", cancellable = true)
        private void chrysalis$blockNoteBlockInstrumentTransformer(CallbackInfoReturnable<NoteBlockInstrument> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockSoundTransformer> registry = Chrysalis.registryAccess.lookupOrThrow(ChrysalisRegistry.BLOCK_SOUND_TRANSFORMER).stream().filter(getBlockState -> getBlockState.blocks().contains(this.asState().getBlockHolder())).findFirst();
            if (registry.isPresent() && registry.get().blocks().contains(this.asState().getBlockHolder())) cir.setReturnValue(BlockSoundTransformer.getNoteBlockInstrument(registry.get().noteBlockInstrument()));
        }

        /**
         * Pulls information from the data-driven block properties system and applies it as the block's properties.
         **/

        @Inject(at = @At("HEAD"), method = "getDestroySpeed", cancellable = true)
        private void chrysalis$blockDestroyTimeTransformer(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyTransformer> optional = this.getBlockPropertyTransformer(this.asState());
            if (optional.isPresent() && optional.get().blocks().contains(this.asState().getBlockHolder())) cir.setReturnValue(optional.get().destroyTime());
        }

        @Inject(at = @At("HEAD"), method = "requiresCorrectToolForDrops", cancellable = true)
        private void chrysalis$blockRequiresToolTransformer(CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyTransformer> optional = this.getBlockPropertyTransformer(this.asState());
            if (optional.isPresent() && optional.get().blocks().contains(this.asState().getBlockHolder())) cir.setReturnValue(optional.get().requiresTool());
        }

        @Inject(at = @At("HEAD"), method = "getLightEmission", cancellable = true)
        private void chrysalis$blockLightLevelTransformer(CallbackInfoReturnable<Integer> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyTransformer> optional = this.getBlockPropertyTransformer(this.asState());
            if (optional.isPresent() && optional.get().blocks().contains(this.asState().getBlockHolder())) cir.setReturnValue(optional.get().lightLevel());
        }

        @Inject(at = @At("HEAD"), method = "emissiveRendering", cancellable = true)
        private void chrysalis$blockEmissiveRenderingTransformer(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            BlockState blockState = blockGetter.getBlockState(blockPos);
            Optional<BlockPropertyTransformer> optional = this.getBlockPropertyTransformer(blockState);
            if (optional.isPresent() && optional.get().blocks().contains(blockState.getBlockHolder())) cir.setReturnValue(optional.get().emissiveRendering());
        }

        @Inject(at = @At("HEAD"), method = "canBeReplaced()Z", cancellable = true)
        private void chrysalis$blockReplaceableTransformer(CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyTransformer> optional = this.getBlockPropertyTransformer(this.asState());
            if (optional.isPresent() && optional.get().blocks().contains(this.asState().getBlockHolder())) cir.setReturnValue(optional.get().replaceable());
        }

        @Inject(at = @At("HEAD"), method = "ignitedByLava", cancellable = true)
        private void chrysalis$blockIgnitedByLavaTransformer(CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyTransformer> optional = this.getBlockPropertyTransformer(this.asState());
            if (optional.isPresent() && optional.get().blocks().contains(this.asState().getBlockHolder())) cir.setReturnValue(optional.get().ignitedByLava());
        }

        @Inject(at = @At("HEAD"), method = "shouldSpawnTerrainParticles", cancellable = true)
        private void chrysalis$blockSpawnsTerrainParticlesTransformer(CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyTransformer> optional = this.getBlockPropertyTransformer(this.asState());
            if (optional.isPresent() && optional.get().blocks().contains(this.asState().getBlockHolder())) cir.setReturnValue(optional.get().spawnsTerrainParticles());
        }

        @Unique
        private Optional<BlockPropertyTransformer> getBlockPropertyTransformer(BlockState blockState) {
            return Chrysalis.registryAccess.lookupOrThrow(ChrysalisRegistry.BLOCK_PROPERTY_TRANSFORMER).stream().filter(getBlockState -> getBlockState.blocks().contains(blockState.getBlockHolder())).findFirst();
        }

        /**
         * Fixes fluid hitboxes for any entity that is able to walk on fluids.
         **/

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
            if (collisionContext.isAbove(voxelShapes[amount - 1], blockPos, true) && collisionContext.canStandOnFluid(blockGetter.getFluidState(blockPos.above()), fluidState)) cir.setReturnValue(Shapes.or(cir.getReturnValue(), voxelShapes[amount]));
        }
    }

    @SuppressWarnings("unused")
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
}