package net.junebug.chrysalis.util.technical.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import java.util.stream.Stream;
import java.io.BufferedReader;

@OnlyIn(Dist.CLIENT)
public class FileLoader {

    private final FileReader<JsonElement> JSON;
    private final FileReader<Stream<String>> RAW;

    public FileLoader() {
        this.JSON = new FileReader<>(JsonParser::parseReader);
        this.RAW = new FileReader<>(BufferedReader::lines);
    }

    public FileReader<JsonElement> json() {
        return this.JSON;
    }

    public FileReader<Stream<String>> raw() {
        return this.RAW;
    }

    public void load(ResourceManager resourceManager) {
        this.JSON.load(resourceManager);
        this.RAW.load(resourceManager);
    }
}