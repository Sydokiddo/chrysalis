package net.sydokiddo.chrysalis.mixin.entities.enderman;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderMan.class)
public class EndermanMixin {

    /**
     * Items that can be worn in the helmet slot to protect against staring at Endermen is now driven by the protects_against_endermen tag.
     **/

    @Redirect(method = "isLookingAtMe", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean chrysalis_carvedPumpkinEndermanTag(ItemStack itemStack, Item item) {
        return itemStack.is(ChrysalisTags.PROTECTS_AGAINST_ENDERMEN);
    }
}
