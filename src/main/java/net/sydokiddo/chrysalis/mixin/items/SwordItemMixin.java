package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.Tool;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Mixin(SwordItem.class)
public class SwordItemMixin {

    /**
     * Any blocks in the mineable/sword tag will be mined faster with Swords.
     **/

    @Inject(method = "createToolProperties", at = @At(value = "RETURN"), cancellable = true)
    private static void chrysalis$createSwordProperties(CallbackInfoReturnable<Tool> cir) {
        cir.setReturnValue(new Tool(List.of(Tool.Rule.minesAndDrops(ChrysalisTags.MINEABLE_WITH_SWORD, 15.0F), Tool.Rule.overrideSpeed(ChrysalisTags.MINEABLE_WITH_SWORD, 1.5F)), 1.0F, 1));
    }
}