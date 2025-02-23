package net.sydokiddo.chrysalis.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ChrysalisCoreRegistry<T> {

    /**
     * Custom core registry created by ItsBlackGear (<a href="https://github.com/ItsBlackGear">...</a>)
     * Thank you for that! :)
     **/

    private final ResourceKey<Registry<T>> key;
    private final String modId;
    private final Registry<T> registry;
    private boolean isPresent;

    @SuppressWarnings("unchecked, rawtypes")
    public ChrysalisCoreRegistry(ResourceKey<Registry<T>> key, String modId) {
        this.key = key;
        this.modId = modId;
        this.isPresent = false;
        this.registry = BuiltInRegistries.REGISTRY.getValue((ResourceKey) key);
    }

    public static <T> ChrysalisCoreRegistry<T> create(ResourceKey<Registry<T>> key, String modId) {
        return new ChrysalisCoreRegistry<>(key, modId);
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