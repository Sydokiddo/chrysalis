package net.sydokiddo.chrysalis.misc.util.splash_texts;

import com.google.gson.JsonElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.file.SimpleFileLoader;
import net.sydokiddo.chrysalis.misc.util.splash_texts.types.SimpleSplashText;
import net.sydokiddo.chrysalis.misc.util.splash_texts.types.SplashText;
import net.sydokiddo.chrysalis.misc.util.splash_texts.types.SplashTextGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class SplashTextLoader implements SimpleFileLoader {

    private SplashTextLoader() {}

    public static final SplashTextLoader INSTANCE = new SplashTextLoader();
    private final List<SplashText> splashTexts = new ArrayList<>();
    private int maxWeight = 0;

    @Override
    public ResourceLocation getFabricId() {
        return Chrysalis.id("splashes");
    }

    public List<SplashText> getSplashTexts() {
        return this.splashTexts;
    }

    public int getMaxWeight() {
        return this.maxWeight;
    }

    @Override
    public void init(SimpleFileLoader.DataFileLoader dataFileLoader, ResourceManager resourceManager) {
        dataFileLoader.json().find("texts/splashes.json", this::addSplashes);
        dataFileLoader.raw().get(ResourceLocation.withDefaultNamespace("texts/splashes.txt"), this::addVanillaSplashes);
    }

    private void addSplashes(ResourceLocation resourceLocation, JsonElement jsonElement) {

        if (!jsonElement.isJsonObject()) {
            Chrysalis.LOGGER.error("Unable to load splash text file: {}, file must be an object containing splash text data", resourceLocation);
            return;
        } else {
            Chrysalis.LOGGER.info("Detected splash text file: {}", resourceLocation);
        }

        SplashTextGroup.fromJson(jsonElement.getAsJsonObject()).getTexts().filter(SplashText::validate).forEach(this::addSplashes);
    }

    private void addSplashes(SplashText splashText) {
        this.maxWeight += splashText.getWeight();
        this.splashTexts.add(splashText);
    }

    private void addVanillaSplashes(Stream<String> stream) {
        stream.map(SimpleSplashText::new).forEach(this::addSplashes);
    }
}