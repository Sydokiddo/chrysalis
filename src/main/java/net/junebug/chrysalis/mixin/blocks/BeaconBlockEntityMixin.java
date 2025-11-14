package net.junebug.chrysalis.mixin.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.junebug.chrysalis.common.misc.CTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {

    /**
     * Blocks that beacon beams can pass through is now driven by the allows_beacon_beam_passthrough tag.
     **/

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static boolean chrysalis$allowsBeaconBeamPassthroughTag(BlockState blockState, Block block) {
        return blockState.is(CTags.ALLOWS_BEACON_BEAM_PASSTHROUGH);
    }
}