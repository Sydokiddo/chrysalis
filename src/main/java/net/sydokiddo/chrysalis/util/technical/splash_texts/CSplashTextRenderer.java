package net.sydokiddo.chrysalis.util.technical.splash_texts;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
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
    public void render(@NotNull GuiGraphics guiGraphics, int x, @NotNull Font font, int y) {
        SplashTextLoader.renderSplashText(guiGraphics, x, font, y, false, null, this.splashText);
    }
}