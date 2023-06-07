package net.sydokiddo.chrysalis.mixin.entities.golems;

import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IronGolem.class)
public class IronGolemMixin {

    // Items that repair Iron Golems is now a tag-based system

    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean chrysalis_ironGolemRepairMaterialTag(ItemStack itemStack, Item item) {
        return itemStack.is(ChrysalisTags.REPAIRS_IRON_GOLEMS);
    }
}
