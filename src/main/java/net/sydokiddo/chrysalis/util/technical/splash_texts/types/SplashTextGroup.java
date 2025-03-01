package net.sydokiddo.chrysalis.util.technical.splash_texts.types;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Style;
import java.util.List;
import com.mojang.serialization.Codec;
import java.util.stream.Stream;

public record SplashTextGroup(Style defaultStyle, List<SplashText> splashTexts) {

    public static final Codec<SplashTextGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Style.Serializer.CODEC.optionalFieldOf("default_style", Style.EMPTY).forGetter(SplashTextGroup::defaultStyle),
        SplashText.CODEC.listOf().fieldOf("entries").forGetter(SplashTextGroup::splashTexts)
    ).apply(instance, SplashTextGroup::new));

    public static SplashTextGroup fromJson(JsonObject jsonObject) {
        return CODEC.parse(JsonOps.INSTANCE, jsonObject).getOrThrow();
    }

    public Stream<SplashText> getTexts() {
        Stream<SplashText> result = this.splashTexts.stream();
        if (this.defaultStyle != Style.EMPTY) result = result.map(text -> text.setStyle(this.defaultStyle));
        return result;
    }
}