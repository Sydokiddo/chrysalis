package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Axolotl.class)
public class AxolotlMixin {

    /**
     * If an axolotl is fed any items in the buckets tag, it will return a water bucket.
     **/

    @Redirect(method = "usePlayerItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean chrysalis$giveBackBucketWhenFeedingAxolotl(ItemStack itemStack, Item item) {
        return itemStack.is(Tags.Items.BUCKETS) && !itemStack.is(Tags.Items.BUCKETS_EMPTY);
    }
}