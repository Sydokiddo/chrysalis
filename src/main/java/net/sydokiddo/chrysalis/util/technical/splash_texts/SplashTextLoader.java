package net.sydokiddo.chrysalis.util.technical.splash_texts;

import com.google.gson.JsonElement;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.technical.file.SimpleFileLoader;
import net.sydokiddo.chrysalis.util.technical.splash_texts.types.SimpleSplashText;
import net.sydokiddo.chrysalis.util.technical.splash_texts.types.SplashText;
import net.sydokiddo.chrysalis.util.technical.splash_texts.types.SplashTextGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class SplashTextLoader implements SimpleFileLoader {

    /**
     * Loads custom splash texts from a json file.
     **/

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

    public static int lifeTime = 15;

    public static void renderSplashText(GuiGraphics guiGraphics, int x, Font font, int y, boolean original, String string, Component component) {

        if (SplashTextLoader.lifeTime < 15) SplashTextLoader.lifeTime++;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) x / 2.0F + 123.0F, 69.0F, 0.0F);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-20.0F));

        float scale = (SplashTextLoader.lifeTime / 15.0F) * (1.8F - Mth.abs(Mth.sin((float) (Util.getMillis() % 1000L) / 1000.0F * 6.2831855F) * 0.1F));

        if (original && string != null) scale = scale * 100.0F / (float) (font.width(string) + 32);
        else if (component != null) scale = scale * 100.0F / (float) (font.width(component) + 32);
        guiGraphics.pose().scale(scale, scale, scale);

        if (original && string != null) guiGraphics.drawCenteredString(font, string, 0, -8, 16776960 | y);
        else if (component != null) guiGraphics.drawCenteredString(font, component, 0, -8, 16776960 | y);
        guiGraphics.pose().popPose();
    }
}