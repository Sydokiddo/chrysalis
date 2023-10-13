package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {

    /**
     * Blocks that Beacon beams can pass through is now driven by the allows_beacons_beam_passthrough
     **/

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static boolean chrysalis$allowsBeaconBeamPassthroughTag(BlockState blockState, Block block) {
        return blockState.is(ChrysalisTags.ALLOWS_BEACON_BEAM_PASSTHROUGH);
    }
}