package net.sydokiddo.chrysalis.util.technical.splash_texts.types;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.util.technical.splash_texts.CSplashTextRenderer;
import java.awt.*;
import java.util.function.UnaryOperator;
import com.mojang.serialization.Codec;
import net.sydokiddo.chrysalis.util.technical.splash_texts.SplashTextLoader;

@OnlyIn(Dist.CLIENT)
public class AdvancedSplashText implements SplashText {

    private static final String defaultFont = "minecraft:default";
    private static final String defaultColor = "#FFFF55";

    public static final Codec<AdvancedSplashText> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ComponentSerialization.CODEC.fieldOf("text").forGetter(null),
        Codec.STRING.optionalFieldOf("font", defaultFont).forGetter(null),
        Codec.STRING.optionalFieldOf("color", defaultColor).forGetter(null),
        Codec.BOOL.optionalFieldOf("bold", false).forGetter(null),
        Codec.BOOL.optionalFieldOf("italic", false).forGetter(null),
        Codec.BOOL.optionalFieldOf("underlined", false).forGetter(null),
        Codec.BOOL.optionalFieldOf("strikethrough", false).forGetter(null),
        Codec.BOOL.optionalFieldOf("obfuscated", false).forGetter(null),
        ExtraCodecs.intRange(1, SplashTextLoader.defaultMaxWeight).orElse(SplashTextLoader.defaultMaxWeight).optionalFieldOf("weight", SplashTextLoader.defaultWeight).forGetter(null)
    ).apply(instance, AdvancedSplashText::new));

    private MutableComponent text;
    private final String font;
    private final String color;
    private final boolean bold;
    private final boolean italic;
    private final boolean underlined;
    private final boolean strikethrough;
    private final boolean obfuscated;
    private final int weight;

    public AdvancedSplashText(Component splashText, String font, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated, int weight) {
        this.text = (MutableComponent) splashText;
        this.font = font;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.weight = weight;
    }

    public AdvancedSplashText(MutableComponent mutableComponent) {
        this(mutableComponent, String.valueOf(ResourceLocation.parse(defaultFont)), defaultColor, false, false, false, false, false, SplashTextLoader.defaultWeight);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public int getWeight() {
        return Math.min(this.weight * SplashTextLoader.INSTANCE.getTotalWeight(), SplashTextLoader.defaultMaxWeight);
    }

    @Override
    public SplashText setStyle(Style style) {
        this.modifyText(text -> text.withStyle(style.withFont(ResourceLocation.parse(this.font)).withColor(Color.decode(this.color).getRGB())
        .withBold(this.bold).withItalic(this.italic).withUnderlined(this.underlined).withStrikethrough(this.strikethrough).withObfuscated(this.obfuscated)));
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