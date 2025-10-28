package net.junebug.chrysalis.mixin.items;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.junebug.chrysalis.common.misc.CTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {

    /**
     * Any blocks in the mineable/shears tags will be mined faster with shears.
     **/

    @Inject(method = "createToolProperties", at = @At(value = "RETURN"), cancellable = true)
    private static void chrysalis$createShearProperties(CallbackInfoReturnable<Tool> cir) {

        HolderGetter<Block> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);

        cir.setReturnValue(
            new Tool(List.of(
                Tool.Rule.minesAndDrops(holderGetter.getOrThrow(CTags.MINEABLE_WITH_SHEARS_FAST), 15.0F),
                Tool.Rule.minesAndDrops(holderGetter.getOrThrow(CTags.MINEABLE_WITH_SHEARS_NORMAL), 5.0F),
                Tool.Rule.minesAndDrops(holderGetter.getOrThrow(CTags.MINEABLE_WITH_SHEARS_SLOW), 2.0F)
            ), 1.0F, 1)
        );
    }

    @Redirect(method = "mineBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean chrysalis$mineBlockWithShears(BlockState blockState, Block block) {
        return blockState.is(CTags.MINEABLE_WITH_SHEARS);
    }
}