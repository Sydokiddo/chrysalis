package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void chrysalis$addSpaceOnShieldBannerTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci) {
        CompoundTag compoundTag = BlockItem.getBlockEntityData(itemStack);
        if (itemStack.isEnchanted() && (compoundTag != null && compoundTag.contains("Patterns"))) tooltip.add(CommonComponents.EMPTY);
    }
}