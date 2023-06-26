package net.sydokiddo.chrysalis.mixin.entities.animals.birds;

import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Parrot.class)
public class ParrotMixin {

    // Items that poison Parrots is now a tag-based system

    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean chrysalis_poisonsParrotsItemTag(ItemStack itemStack, Item item) {
        return itemStack.is(ChrysalisTags.POISONS_PARROTS);
    }
}
