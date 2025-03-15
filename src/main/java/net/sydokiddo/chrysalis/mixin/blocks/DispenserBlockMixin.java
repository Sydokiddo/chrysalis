package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.sydokiddo.chrysalis.util.blocks.dispensers.DispenseContainerMobDispenserBehavior;
import net.sydokiddo.chrysalis.util.blocks.dispensers.DispenseCustomSpawnEggDispenserBehavior;
import net.sydokiddo.chrysalis.common.items.custom_items.CSpawnEggItem;
import net.sydokiddo.chrysalis.common.items.custom_items.MobInContainerItem;
import net.sydokiddo.chrysalis.common.items.custom_items.MobInFluidBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    /**
     * Adds new dispenser methods.
     **/

    @Inject(method = "getDispenseMethod", at = @At("HEAD"), cancellable = true)
    private void chrysalis$addNewDispenserMethods(Level level, ItemStack itemStack, CallbackInfoReturnable<DispenseItemBehavior> cir) {
        Item item = itemStack.getItem();
        if (item instanceof CSpawnEggItem) cir.setReturnValue(DispenseCustomSpawnEggDispenserBehavior.INSTANCE);
        if (item instanceof MobInContainerItem || item instanceof MobInFluidBucketItem) cir.setReturnValue(DispenseContainerMobDispenserBehavior.INSTANCE);
    }
}