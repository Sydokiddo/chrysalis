package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.common.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IronGolem.class)
public class IronGolemMixin {

    /**
     * Items that can repair damaged iron golems is now driven by the repairs_iron_golems tag.
     **/

    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean chrysalis$ironGolemRepairMaterialTag(ItemStack itemStack, Item item) {
        return itemStack.is(ChrysalisTags.REPAIRS_IRON_GOLEMS);
    }
}