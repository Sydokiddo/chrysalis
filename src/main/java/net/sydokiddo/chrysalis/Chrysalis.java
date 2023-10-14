package net.sydokiddo.chrysalis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chrysalis implements ModInitializer {

	public static final String MOD_ID = "chrysalis";
	public static final String chrysalisVersion = "v0.2.1";

	public static final Logger LOGGER = LoggerFactory.getLogger("Chrysalis");
	public static final boolean IS_DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();

	@SuppressWarnings("unused")
	public static final boolean CHRYSALIS_INITIALIZED = FabricLoader.getInstance().isModLoaded(MOD_ID);

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ChrysalisRegistry.registerAll();
		LOGGER.info("Chrysalis " + chrysalisVersion + " has been initialized!");
	}
}