package net.sydokiddo.chrysalis.mixin.blocks.chorus_plant;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChorusPlantBlock.class)
public class ChorusPlantBlockMixin {

    /**
     * Chorus Plants are now placeable on any blocks in the chorus_plant_can_grow_on tag.
     **/

    @Redirect(method = "getStateWithConnections", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    private static boolean chrysalis$chorusPlantGetStateWithConnections(BlockState blockState, Block block) {
        return blockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }

    @Redirect(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    public boolean chrysalis$chorusPlantUpdateShape(BlockState blockState, Block block) {
        return blockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }

    @Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 1))
    private boolean chrysalis$chorusPlantCanSurvive1(BlockState blockState, Block block) {
        return blockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }

    @Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 4))
    private boolean chrysalis$chorusPlantCanSurvive2(BlockState blockState, Block block) {
        return blockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }
}