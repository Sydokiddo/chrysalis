package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(ShieldItem.class)
public class ShieldItemMixin extends Item {

    private ShieldItemMixin(Properties properties) {
        super(properties);
    }

    /**
     * Cleans up the shield banner pattern tooltip layout.
     **/

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void chrysalis$changeShieldTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo info) {

        if (!CConfigOptions.REWORKED_TOOLTIPS.get()) return;
        info.cancel();
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);

        BannerPatternLayers bannerPatternLayers = itemStack.get(DataComponents.BANNER_PATTERNS);

        if (bannerPatternLayers != null) {

            if (!bannerPatternLayers.layers().isEmpty()) {
                list.add(CommonComponents.EMPTY);
                list.add(Component.translatable("gui.chrysalis.item.banner_patterns").withStyle(ChatFormatting.GRAY));
            }

            for (int layers = 0; layers < Math.min(bannerPatternLayers.layers().size(), 6); layers++) {
                list.add(CommonComponents.space().append(bannerPatternLayers.layers().get(layers).description().withStyle(ChatFormatting.BLUE)));
            }
        }
    }
}