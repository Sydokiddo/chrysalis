package net.sydokiddo.chrysalis.util.technical.splash_texts.types;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Style;
import net.sydokiddo.chrysalis.util.technical.CodecUtils;

public interface SplashText {

    Codec<SplashText> CODEC = CodecUtils.merge(SimpleSplashText.CODEC, AdvancedSplashText.CODEC);

    boolean validate();
    int getWeight();
    SplashText setStyle(Style style);
    SplashRenderer renderer();
}