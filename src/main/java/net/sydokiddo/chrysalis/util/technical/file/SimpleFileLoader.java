package net.sydokiddo.chrysalis.util.technical.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.server.packs.resources.ResourceManager;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import java.util.concurrent.CompletableFuture;
import java.io.BufferedReader;

public interface SimpleFileLoader extends SimpleResourceReloadListener<Void> {

    void init(DataFileLoader dataFileLoader, ResourceManager resourceManager);

    default void load(ResourceManager resourceManager) {
        DataFileLoader dataFileLoader = new DataFileLoader();
        this.init(dataFileLoader, resourceManager);
        dataFileLoader.load(resourceManager);
    }

    class DataFileLoader {

        private final FileReader<JsonElement> JSON;
        private final FileReader<Stream<String>> RAW;

        public DataFileLoader() {
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

    @Override
    default CompletableFuture<Void> load(ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.runAsync(() -> this.load(resourceManager), executor);
    }

    @Override
    default CompletableFuture<Void> apply(Void data, ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.runAsync(() -> {}, executor);
    }
}