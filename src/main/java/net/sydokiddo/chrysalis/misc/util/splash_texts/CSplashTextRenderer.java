package net.sydokiddo.chrysalis.misc.util.splash_texts;

import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class CSplashTextRenderer extends SplashRenderer {

    private final Component splashText;

    public CSplashTextRenderer(Component splashText) {
        super("");
        this.splashText = splashText;
    }

    public void render(GuiGraphics guiGraphics, int x, Font font, int y) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) x / 2.0F + 123.0F, 69.0F, 0.0F);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-20.0F));
        float scale = 1.8F - Mth.abs(Mth.sin((float) (Util.getMillis() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
        scale = scale * 100.0F / (float) (font.width(this.splashText) + 32);
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.drawCenteredString(font, this.splashText, 0, -8, 16776960 | y);
        guiGraphics.pose().popPose();
    }
}