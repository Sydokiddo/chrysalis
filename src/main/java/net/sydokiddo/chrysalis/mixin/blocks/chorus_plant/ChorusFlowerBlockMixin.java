package net.sydokiddo.chrysalis.mixin.blocks.chorus_plant;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerBlockMixin {

    /**
     * Chorus Flowers are now placeable on and tick on any blocks in the chorus_plant_can_grow_on tag.
     **/

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    private boolean chrysalis$chorusFlowerRandomTick1(BlockState blockState, Block block) {
        return blockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 3))
    private boolean chrysalis$chorusFlowerRandomTick2(BlockState blockState, Block block) {
        return blockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }

    @Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 1))
    private boolean chrysalis$chorusFlowerCanSurvive(BlockState blockState, Block block) {
        return blockState.is(ChrysalisTags.CHORUS_PLANT_CAN_GROW_ON);
    }
}