package net.sydokiddo.chrysalis.util.technical.file;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.io.BufferedReader;

@OnlyIn(Dist.CLIENT)
public class FileReader<Value> {

    private final Function<BufferedReader, Value> bufferReader;

    public FileReader(Function<BufferedReader, Value> bufferReader) {
        this.bufferReader = bufferReader;
    }

    public record FilePath(String basePath, Function<ResourceLocation, Boolean> predicate) {
        public static FilePath simple(String string) {
            return new FilePath(string, path -> true);
        }
    }

    private final HashMap<ResourceLocation, Consumer<Value>> getFiles = new HashMap<>();
    private final HashMap<FilePath, BiConsumer<ResourceLocation, Value>> findFiles = new HashMap<>();

    void load(ResourceManager resourceManager) {
        this.getFiles.forEach((resourceLocation, consumer) -> this.getFile(resourceManager, resourceLocation, consumer));
        this.findFiles.forEach((filePath, consumer) -> this.findFiles(resourceManager, filePath, consumer));
    }

    private void getFile(ResourceManager resourceManager, ResourceLocation resourceLocation, Consumer<Value> consumer) {
        Optional<Resource> resource = resourceManager.getResource(resourceLocation);
        if (resource.isEmpty()) return;
        consumer.accept(this.fromResource(resource.get()));
    }

    private void findFiles(ResourceManager resourceManager, FilePath filePath, BiConsumer<ResourceLocation, Value> consumer) {
        resourceManager.listResources(filePath.basePath(), filePath.predicate::apply).forEach((identifier, resource) -> consumer.accept(identifier, this.fromResource(resource)));
    }

    private Value fromResource(Resource resource) {
        try {
            return this.bufferReader.apply(resource.openAsReader());
        }
        catch (IOException ignored) {}
        return null;
    }

    public void get(ResourceLocation resourceLocation, Consumer<Value> consumer) {
        this.getFiles.put(resourceLocation, consumer);
    }

    public void find(String string, BiConsumer<ResourceLocation, Value> consumer) {
        this.findFiles.put(FilePath.simple(string), consumer);
    }
}