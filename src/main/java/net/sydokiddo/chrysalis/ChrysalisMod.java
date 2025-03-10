package net.sydokiddo.chrysalis;

import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.util.helpers.CompatibilityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ChrysalisMod.MOD_ID)
public class ChrysalisMod {

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

	public ChrysalisMod() {
		ChrysalisRegistry.registerAll();
		ChrysalisMod.sendInitializedMessage(false);
	}

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
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
}