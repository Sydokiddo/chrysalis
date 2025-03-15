package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.common.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Dolphin.class)
public class DolphinMixin {

    /**
     * Items that can be fed to dolphins is now controlled by the dolphin_foods tag rather than the fishes tag.
     **/

    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean chrysalis$dolphinFoodTag(ItemStack itemStack, TagKey<Item> tagKey) {
        return itemStack.is(ChrysalisTags.DOLPHIN_FOODS);
    }
}