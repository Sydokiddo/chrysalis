package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void chrysalis$addSpaceOnShieldBannerTooltip(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo info) {

        CompoundTag compoundTag = BlockItem.getBlockEntityData(itemStack);

        if (itemStack.isEnchanted() && (compoundTag != null && compoundTag.contains("Patterns"))) {
            tooltip.add(CommonComponents.EMPTY);
        }
    }
}