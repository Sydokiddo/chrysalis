package net.sydokiddo.chrysalis.misc.util.splash_texts.types;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ExtraCodecs;
import net.sydokiddo.chrysalis.misc.util.splash_texts.CSplashTextRenderer;
import java.awt.*;
import java.util.function.UnaryOperator;
import com.mojang.serialization.Codec;

public class AdvancedSplashText implements SplashText {

    private static final String defaultColor = "#FFFF55";
    public static final int defaultWeight = 1;

    public static final Codec<AdvancedSplashText> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ComponentSerialization.CODEC.fieldOf("text").forGetter(null),
        Codec.STRING.optionalFieldOf("color", defaultColor).forGetter(null),
        ExtraCodecs.intRange(1, 1000).optionalFieldOf("weight", defaultWeight).forGetter(null)
    ).apply(instance, AdvancedSplashText::new));

    private MutableComponent text;
    private final String color;
    private final int weight;

    public AdvancedSplashText(Component splashText, String color, Integer weight) {
        this.text = (MutableComponent) splashText;
        this.color = color;
        this.weight = weight;
    }

    public AdvancedSplashText(MutableComponent mutableComponent) {
        this(mutableComponent, defaultColor, defaultWeight);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public SplashText setStyle(Style style) {
        this.modifyText(text -> text.withStyle(style.withColor(Color.decode(this.color).getRGB())));
        return this;
    }

    public void modifyText(UnaryOperator<MutableComponent> unaryOperator) {
        this.text = unaryOperator.apply(this.text);
    }

    @Override
    public String toString() {
        return "AdvancedSplashText["+this.text.getString()+"]";
    }

    @Override
    public SplashRenderer renderer() {
        return new CSplashTextRenderer(this.text);
    }
}