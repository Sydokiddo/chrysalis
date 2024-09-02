package net.sydokiddo.chrysalis.misc.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("all")
public class CoreRegistry<T> {

    /**
     * Custom core registry created by ItsBlackGear (<a href="https://github.com/ItsBlackGear">...</a>)
     * Thank you for that! :)
     **/

    private final ResourceKey<Registry<T>> key;
    private final String modId;
    private final Registry<T> registry;
    private boolean isPresent;

    public CoreRegistry(ResourceKey<Registry<T>> key, String modId) {
        this.key = key;
        this.modId = modId;
        this.isPresent = false;
        this.registry = BuiltInRegistries.REGISTRY.get((ResourceKey)key);
    }

    public static <T> CoreRegistry<T> create(ResourceKey<Registry<T>> key, String modId) {
        return new CoreRegistry<>(key, modId);
    }

    public <E extends T> E register(String key, E entry) {
        Registry.register(this.registry, ResourceLocation.fromNamespaceAndPath(this.modId, key), entry);
        return entry;
    }

    public void register() {
        if (this.isPresent) throw new IllegalStateException("Duplicate of Registry: " + this.key);
        this.isPresent = true;
    }
}