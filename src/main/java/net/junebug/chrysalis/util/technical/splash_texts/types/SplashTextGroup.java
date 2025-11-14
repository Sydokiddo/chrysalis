package net.junebug.chrysalis.util.technical.splash_texts.types;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Style;
import java.util.List;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.junebug.chrysalis.util.technical.splash_texts.SplashTextLoader;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public record SplashTextGroup(int totalWeight, Style defaultStyle, List<SplashText> splashTexts) {

    /**
     * The base class for loading splash text groups.
     **/

    public static final Codec<SplashTextGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.intRange(SplashTextLoader.defaultMinWeight, SplashTextLoader.defaultMaxWeight).orElse(SplashTextLoader.defaultMaxWeight).optionalFieldOf("total_weight", SplashTextLoader.defaultWeight).forGetter(SplashTextGroup::totalWeight),
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