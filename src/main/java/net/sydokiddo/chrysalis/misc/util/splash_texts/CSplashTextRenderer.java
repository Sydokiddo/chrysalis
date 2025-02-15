package net.sydokiddo.chrysalis.misc.util.splash_texts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class CSplashTextRenderer extends SplashRenderer {

    /**
     * Renders the custom splash text.
     **/

    private final Component splashText;

    public CSplashTextRenderer(Component splashText) {
        super("");
        this.splashText = splashText;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, Font font, int y) {
        SplashTextLoader.renderSplashText(guiGraphics, x, font, y, false, null, this.splashText);
    }
}