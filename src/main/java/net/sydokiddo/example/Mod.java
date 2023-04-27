package net.sydokiddo.example;

import net.fabricmc.api.ModInitializer;
import net.sydokiddo.example.registry.ModRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod implements ModInitializer {

	public static final String MOD_ID = "modid";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistry.registerAll();
		LOGGER.info("Thank you for downloading Example Mod");
	}
}
