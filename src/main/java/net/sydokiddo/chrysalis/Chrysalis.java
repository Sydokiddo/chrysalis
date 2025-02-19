package net.sydokiddo.chrysalis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

public class Chrysalis implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("Chrysalis");
	public static RegistryAccess registryAccess;

	public static final String
		MOD_ID = "chrysalis",
		CHRYSALIS_VERSION = getModContainer().isPresent() ? getModContainer().get().getMetadata().getVersion().getFriendlyString() : "???"
	;

	@SuppressWarnings("unused")
	public static final boolean
		IS_DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment(),
		CHRYSALIS_INITIALIZED = FabricLoader.getInstance().isModLoaded(MOD_ID)
	;

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	public static <T> ResourceKey<Registry<T>> key(String name) {
		return ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace(name));
	}

	private static Optional<ModContainer> getModContainer() {
		return FabricLoader.getInstance().getModContainer(MOD_ID);
	}

	@Override
	public void onInitialize() {
		ChrysalisRegistry.registerAll();
		LOGGER.info("{} v{} has been initialized!", LOGGER.getName(), CHRYSALIS_VERSION);
	}
}