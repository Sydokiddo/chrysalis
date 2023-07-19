package net.sydokiddo.chrysalis;

import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chrysalis implements ModInitializer {

	public static final String MOD_ID = "chrysalis";
	public static final String chrysalisVersion = "v0.1.2";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean IS_DEBUG = QuiltLoader.isDevelopmentEnvironment();

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	@Override
	public void onInitialize(ModContainer mod) {
		ModRegistry.registerAll();
		LOGGER.info("Chrysalis library has been initialized!");
	}
}