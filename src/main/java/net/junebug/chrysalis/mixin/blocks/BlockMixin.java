package net.junebug.chrysalis.mixin.blocks;

import net.junebug.chrysalis.common.entities.custom_entities.effects.earthquake.Earthquake;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.CRegistry;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.util.blocks.codecs.BlockPropertyData;
import net.junebug.chrysalis.util.sounds.codecs.BlockSoundData;
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
    private void chrysalis$vanillaBlockSoundData(BlockState blockState, CallbackInfoReturnable<SoundType> cir) {
        if (BlockSoundData.getFinalSoundType(blockState) != null) cir.setReturnValue(BlockSoundData.getFinalSoundType(blockState));
    }

    @Mixin(BlockBehaviour.BlockStateBase.class)
    public static abstract class BlockStateBaseMixin {

        @Shadow protected abstract BlockState asState();
        @Shadow public abstract boolean is(TagKey<Block> tag);

        /**
         * Pulls the specified note block instrument from the json file and applies it as the block's note block instrument.
         **/

        @Inject(at = @At("HEAD"), method = "instrument", cancellable = true)
        private void chrysalis$blockNoteBlockInstrumentData(CallbackInfoReturnable<NoteBlockInstrument> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockSoundData> optional = Chrysalis.registryAccess.lookupOrThrow(CRegistry.BLOCK_SOUND_DATA).stream().filter(codec -> codec.blocks().contains(this.asState().getBlockHolder())).findFirst();
            if (optional.isPresent() && CRegistry.isDataEnabled(optional.get().enabled()) && BlockSoundData.getNoteBlockInstrument(optional.get().noteBlockInstrument()) != null) cir.setReturnValue(BlockSoundData.getNoteBlockInstrument(optional.get().noteBlockInstrument()));
        }

        /**
         * Pulls information from the data-driven block properties system and applies it as the block's properties.
         **/

        @Inject(at = @At("HEAD"), method = "getDestroySpeed", cancellable = true)
        private void chrysalis$blockDestroyTimeData(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());
            if (optional.isPresent() && CRegistry.isDataEnabled(optional.get().enabled())) cir.setReturnValue(optional.get().destroyTime());
        }

        @Inject(at = @At("HEAD"), method = "requiresCorrectToolForDrops", cancellable = true)
        private void chrysalis$blockRequiresToolData(CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());
            if (optional.isPresent() && CRegistry.isDataEnabled(optional.get().enabled())) cir.setReturnValue(optional.get().requiresTool());
        }

        @Inject(at = @At("HEAD"), method = "getLightEmission", cancellable = true)
        private void chrysalis$blockLightLevelData(CallbackInfoReturnable<Integer> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());
            if (optional.isPresent() && CRegistry.isDataEnabled(optional.get().enabled())) cir.setReturnValue(optional.get().lightLevel());
        }

        @Inject(at = @At("HEAD"), method = "emissiveRendering", cancellable = true)
        private void chrysalis$blockEmissiveRenderingDate(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(blockGetter.getBlockState(blockPos));
            if (optional.isPresent() && CRegistry.isDataEnabled(optional.get().enabled())) cir.setReturnValue(optional.get().emissiveRendering());
        }

        @Inject(at = @At("HEAD"), method = "canBeReplaced()Z", cancellable = true)
        private void chrysalis$blockReplaceableData(CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());
            if (optional.isPresent() && CRegistry.isDataEnabled(optional.get().enabled())) cir.setReturnValue(optional.get().replaceable());
        }

        @Inject(at = @At("HEAD"), method = "ignitedByLava", cancellable = true)
        private void chrysalis$blockIgnitedByLavaData(CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());
            if (optional.isPresent() && CRegistry.isDataEnabled(optional.get().enabled())) cir.setReturnValue(optional.get().ignitedByLava());
        }

        @Inject(at = @At("HEAD"), method = "shouldSpawnTerrainParticles", cancellable = true)
        private void chrysalis$blockSpawnsTerrainParticlesData(CallbackInfoReturnable<Boolean> cir) {
            if (Chrysalis.registryAccess == null) return;
            Optional<BlockPropertyData> optional = this.chrysalis$getBlockPropertyData(this.asState());
            if (optional.isPresent() && CRegistry.isDataEnabled(optional.get().enabled())) cir.setReturnValue(optional.get().spawnsTerrainParticles());
        }

        @Unique
        private Optional<BlockPropertyData> chrysalis$getBlockPropertyData(BlockState blockState) {
            return Chrysalis.registryAccess.lookupOrThrow(CRegistry.BLOCK_PROPERTY_DATA).stream().filter(codec -> codec.blocks().contains(blockState.getBlockHolder())).findFirst();
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
        private void chrysalis$modifyCollisionShapes(BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext, CallbackInfoReturnable<VoxelShape> cir) {
            if (collisionContext instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof Earthquake && (blockGetter.getBlockState(blockPos).is(CTags.EARTHQUAKE_BREAKABLE_BLOCKS) || blockGetter.getBlockState(blockPos).is(CTags.EARTHQUAKE_IGNORED_BLOCKS))) {
                cir.setReturnValue(Shapes.empty());
            } else {
                FluidState fluidState = blockGetter.getFluidState(blockPos);
                int amount = fluidState.getAmount();
                if (amount == 0) return;
                if (collisionContext.isAbove(chrysalis$voxelShapes[amount - 1], blockPos, true) && collisionContext.canStandOnFluid(blockGetter.getFluidState(blockPos.above()), fluidState)) cir.setReturnValue(Shapes.or(cir.getReturnValue(), chrysalis$voxelShapes[amount]));
            }
        }
    }

    @Mixin(Blocks.class)
    public static class BlocksRegistryMixin {

        /**
         * Allows for any mobs in the can_spawn_on_leaves tag to be able to spawn on leaves, instead of just the previously hard-coded ocelots or parrots.
         **/

        @Inject(at = @At("HEAD"), method = "ocelotOrParrot", cancellable = true)
        private static void chrysalis$canSpawnOnLeavesEntityTag(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType, CallbackInfoReturnable<Boolean> cir) {
            cir.setReturnValue(entityType.is(CTags.CAN_SPAWN_ON_LEAVES));
        }
    }

    @Mixin(IBlockExtension.class)
    public interface IBlockStateExtensionClass {

        /**
         * Gets the block sound data on neoforge's getSoundType method alongside the vanilla one.
         **/

        @Inject(at = @At("HEAD"), method = "getSoundType", cancellable = true)
        private void chrysalis$neoForgeBlockSoundData(BlockState blockState, LevelReader levelReader, BlockPos blockPos, Entity entity, CallbackInfoReturnable<SoundType> cir) {
            if (BlockSoundData.getFinalSoundType(blockState) != null) cir.setReturnValue(BlockSoundData.getFinalSoundType(blockState));
        }
    }
}