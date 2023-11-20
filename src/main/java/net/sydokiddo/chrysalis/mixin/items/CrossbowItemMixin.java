package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void chrysalis$addSpaceBeforeCrossbowEnchantments(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
        RegistryHelpers.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
    }
}