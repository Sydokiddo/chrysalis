package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.Consumer;

@Mixin(ArmorTrim.class)
public abstract class ArmorTrimMixin {

    @Shadow public abstract boolean showInTooltip();
    @Shadow public abstract Holder<TrimPattern> pattern();
    @Shadow public abstract Holder<TrimMaterial> material();

    @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
    private void chrysalis$changeArmorTrimTooltip(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo info) {
        if (this.showInTooltip()) {
            info.cancel();
            consumer.accept(Component.translatable("gui.chrysalis.item.armor_trim", this.pattern().value().description().copy().withStyle(ChatFormatting.GRAY), this.material().value().description()).withStyle(this.material().value().description().getStyle()));
        }
    }
}