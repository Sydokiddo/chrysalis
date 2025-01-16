package net.sydokiddo.chrysalis.mixin.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.registry.items.custom_items.CustomCrosshairItem;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.Function;

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

    @Environment(EnvType.CLIENT)
    @SuppressWarnings("unused")
    @Mixin(GuiGraphics.class)
    public static abstract class GuiGraphicsMixin {

        @Shadow public abstract PoseStack pose();
        @Shadow public abstract void blitSprite(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int x, int y, int sizeX, int sizeY);

        @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
        private void chrysalis$renderWaxedIcon(Font font, ItemStack itemStack, int offsetX, int offsetY, String string, CallbackInfo info) {
            if (itemStack.is(ChrysalisTags.WAXED_BLOCK_ITEMS)) {
                this.pose().pushPose();
                this.blitSprite(RenderType::guiTexturedOverlay, Chrysalis.id("hud/waxed_icon"), offsetX - 1, offsetY - 1, 8, 8);
                this.pose().popPose();
            }
        }
    }
}