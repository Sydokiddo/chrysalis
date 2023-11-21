package net.sydokiddo.chrysalis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chrysalis implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("Chrysalis");

	@SuppressWarnings("all")
	public static final String
		MOD_ID = "chrysalis",
		CHRYSALIS_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString()
	;

	@SuppressWarnings("unused")
	public static final boolean
		IS_DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment(),
		CHRYSALIS_INITIALIZED = FabricLoader.getInstance().isModLoaded(MOD_ID)
	;

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ChrysalisRegistry.registerAll();
		LOGGER.info(LOGGER.getName() + " v" + CHRYSALIS_VERSION + " has been initialized!");
	}
}