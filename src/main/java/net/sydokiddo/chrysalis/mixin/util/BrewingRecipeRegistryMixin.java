package net.sydokiddo.chrysalis.mixin.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface BrewingRecipeRegistryMixin {

    /**
     * Accesses brewing recipes for easy registering of custom potion recipes.
     **/

    @SuppressWarnings("ALL")
    @Invoker("addMix")
    static void invokeRegisterPotionRecipe(Potion input, Item item, Potion output) {
        throw new AssertionError();
    }
}