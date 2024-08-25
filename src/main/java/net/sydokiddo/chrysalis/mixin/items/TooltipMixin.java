package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin({DiscFragmentItem.class, BannerItem.class, BannerPatternItem.class, FireworkRocketItem.class, MobBucketItem.class, WrittenBookItem.class, PotionItem.class})
public class TooltipMixin extends Item {

    private TooltipMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void chrysalis$allowOriginalTooltips(ItemStack itemStack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo ci) {
        super.appendHoverText(itemStack, context, list, tooltipFlag);
    }
}