package net.sydokiddo.chrysalis;

import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.sydokiddo.chrysalis.common.ChrysalisRegistry;
import net.sydokiddo.chrysalis.util.helpers.CompatibilityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Chrysalis.MOD_ID)
public class Chrysalis {

	public static final Logger LOGGER = LoggerFactory.getLogger("Chrysalis");
	public static RegistryAccess registryAccess;

	public static final String
		MOD_ID = "chrysalis",
		CHRYSALIS_VERSION = FMLLoader.getLoadingModList().getModFileById(MOD_ID).versionString()
	;

	@SuppressWarnings("unused")
	public static final boolean
		IS_DEBUG = SharedConstants.IS_RUNNING_IN_IDE,
		CHRYSALIS_INITIALIZED = CompatibilityHelper.isModLoaded(MOD_ID)
	;

	public Chrysalis(IEventBus eventBus) {
		ChrysalisRegistry.registerAll(eventBus);
		Chrysalis.sendInitializedMessage(false);
	}

	// region Common Methods

	public static ResourceLocation resourceLocationId(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	@SuppressWarnings("unused")
	public static String stringId(String name) {
		return MOD_ID + ":" + name;
	}

	public static <T> ResourceKey<Registry<T>> key(String name) {
		return ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace(name));
	}

	public static void sendInitializedMessage(boolean clientSide) {
		String dist;
		if (clientSide) dist = "Client-Side";
		else dist = "Server-Side";
		LOGGER.info("{} v{} has been initialized! ({})", LOGGER.getName(), CHRYSALIS_VERSION, dist);
	}

	// endregion
}