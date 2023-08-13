package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TntBlock;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TntBlock.class)
public class TNTBlockMixin {

    /**
     * Items that can ignite TNT is now driven by the tnt_igniters tag.
     **/

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean chrysalis_tntIgnitersTag(ItemStack itemStack, Item item) {
        return itemStack.is(ChrysalisTags.TNT_IGNITERS);
    }
}