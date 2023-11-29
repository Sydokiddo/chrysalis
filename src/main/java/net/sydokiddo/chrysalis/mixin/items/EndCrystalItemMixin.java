package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin {

    /**
     * End Crystals are now placeable on any blocks in the end_crystal_base_blocks tag.
     **/

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean chrysalis$changeEndCrystalPlacing(BlockState blockState, Block block){
        return blockState.is(ChrysalisTags.END_CRYSTAL_BASE_BLOCKS);
    }
}