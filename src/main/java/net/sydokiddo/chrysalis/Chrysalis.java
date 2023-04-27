package net.sydokiddo.chrysalis;

import net.fabricmc.api.ModInitializer;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chrysalis implements ModInitializer {

	public static final String MOD_ID = "chrysalis";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistry.registerAll();
		LOGGER.info("Chrysalis library has been initialized!");
	}
}
