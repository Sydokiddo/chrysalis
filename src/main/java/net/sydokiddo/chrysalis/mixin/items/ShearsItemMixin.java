package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {

    /**
     * Any blocks in the mineable/shears tag will be mined faster with Shears.
     **/

    @Inject(method = "createToolProperties", at = @At(value = "RETURN"), cancellable = true)
    private static void chrysalis$createShearProperties(CallbackInfoReturnable<Tool> cir) {
        cir.setReturnValue(new Tool(List.of(Tool.Rule.minesAndDrops(ChrysalisTags.MINEABLE_WITH_SHEARS, 15.0F), Tool.Rule.overrideSpeed(ChrysalisTags.MINEABLE_WITH_SHEARS, 15.0F)), 1.0F, 1));
    }

    @Redirect(method = "mineBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean chrysalis$mineBlockWithShears(BlockState blockState, Block block) {
        return blockState.is(ChrysalisTags.MINEABLE_WITH_SHEARS);
    }
}