package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TntBlock;
import net.sydokiddo.chrysalis.common.misc.CTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TntBlock.class)
public class TNTBlockMixin {

    /**
     * Items that can ignite TNT are now driven by the tnt_igniters tag.
     **/

    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private boolean chrysalis$tntIgnitersTag1(ItemStack itemStack, Item item) {
        return itemStack.is(CTags.TNT_IGNITERS);
    }

    @Redirect(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 1))
    private boolean chrysalis$tntIgnitersTag2(ItemStack itemStack, Item item) {
        return itemStack.is(CTags.TNT_IGNITERS);
    }
}