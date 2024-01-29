package net.sydokiddo.chrysalis.mixin.blocks.chorus_plant;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.levelgen.feature.ChorusPlantFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantFeature.class)
public class ChorusPlantFeatureMixin {

    /**
     * Allows for the Chorus Plant configured feature to be placed on any blocks in the chorus_plant_can_grow_on tag.
     **/

    @Inject(method = "place", at = @At("RETURN"), cancellable = true)
    private void chrysalis$placeChorusPlantFeature(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> cir) {

        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();

        if (worldGenLevel.isEmptyBlock(blockPos) && worldGenLevel.getBlockState(blockPos.below()).is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON)) {
            ChorusFlowerBlock.generatePlant(worldGenLevel, blockPos, featurePlaceContext.random(), 8);
            cir.setReturnValue(true);
        }
    }
}