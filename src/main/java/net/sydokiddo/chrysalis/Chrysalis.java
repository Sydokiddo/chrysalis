package net.sydokiddo.chrysalis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chrysalis implements ModInitializer {

	public static final String MOD_ID = "chrysalis";
	public static final String chrysalisVersion = "v0.1.3";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean IS_DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModRegistry.registerAll();
		LOGGER.info("Chrysalis library has been initialized!");
	}
}