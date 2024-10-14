package net.sydokiddo.chrysalis.misc.util.splash_texts.types;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class SimpleSplashText implements SplashText {

    public static final Codec<SimpleSplashText> CODEC = Codec.STRING.xmap(SimpleSplashText::new, null);
    private final String splashText;

    public SimpleSplashText(String splashText) {
        this.splashText = splashText;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int getWeight() {
        return AdvancedSplashText.defaultWeight;
    }

    @Override
    public SplashText setStyle(Style style) {
        return new AdvancedSplashText(Component.literal(this.splashText)).setStyle(style);
    }

    @Override
    public String toString() {
        return "SimpleSplashText["+this.splashText+"]";
    }

    @Override
    public SplashRenderer renderer() {
        return new SplashRenderer(this.splashText);
    }
}