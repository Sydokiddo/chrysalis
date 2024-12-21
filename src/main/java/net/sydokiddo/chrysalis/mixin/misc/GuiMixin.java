package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.sydokiddo.chrysalis.registry.items.custom_items.CustomCrosshairItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyArg(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 0))
    private ResourceLocation chrysalis$renderCustomCrosshair(ResourceLocation resourceLocation) {

        Player player = this.minecraft.player;

        if (player != null && player.getMainHandItem().getItem() instanceof CustomCrosshairItem customCrosshairItem &&
        customCrosshairItem.shouldDisplayCrosshair(player) && customCrosshairItem.getCrosshairTextureLocation() != null) return customCrosshairItem.getCrosshairTextureLocation();

        if (player != null && player.getOffhandItem().getItem() instanceof CustomCrosshairItem customCrosshairItem &&
        customCrosshairItem.shouldDisplayCrosshair(player) && customCrosshairItem.getCrosshairTextureLocation() != null) return customCrosshairItem.getCrosshairTextureLocation();

        return resourceLocation;
    }
}