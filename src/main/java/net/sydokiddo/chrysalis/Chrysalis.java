package net.sydokiddo.chrysalis;

import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.sydokiddo.chrysalis.common.ChrysalisRegistry;
import net.sydokiddo.chrysalis.util.helpers.CompatibilityHelper;
import net.sydokiddo.chrysalis.util.helpers.DebugHelper;
import net.sydokiddo.chrysalis.util.technical.config.CConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Chrysalis.MOD_ID)
public class Chrysalis {

	public static final Logger LOGGER = LoggerFactory.getLogger("Chrysalis");
	public static RegistryAccess registryAccess;
	public static boolean registerExampleRegistry = false;

	public static final String
		MOD_ID = "chrysalis",
		CHRYSALIS_VERSION = FMLLoader.getLoadingModList().getModFileById(Chrysalis.MOD_ID).versionString()
	;

	@SuppressWarnings("unused")
	public static final boolean
		IS_DEBUG = SharedConstants.IS_RUNNING_IN_IDE,
		CHRYSALIS_INITIALIZED = CompatibilityHelper.isModLoaded(Chrysalis.MOD_ID)
	;

	public Chrysalis(ModContainer container, IEventBus eventBus) {
		ChrysalisRegistry.registerAll(eventBus);
		container.registerConfig(ModConfig.Type.COMMON, CConfig.CONFIG_SPEC);
		DebugHelper.sendInitializedMessage(Chrysalis.LOGGER, Chrysalis.CHRYSALIS_VERSION, false);
	}

	// region Common Methods

	public static ResourceLocation resourceLocationId(String name) {
		return ResourceLocation.fromNamespaceAndPath(Chrysalis.MOD_ID, name);
	}

	public static String stringId(String name) {
		return Chrysalis.MOD_ID + ":" + name;
	}

	public static <T> ResourceKey<Registry<T>> key(String name) {
		return ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace(name));
	}

	// endregion

	@SuppressWarnings("unused")
	@Mod(value = Chrysalis.MOD_ID, dist = Dist.CLIENT)
	public static class ChrysalisClient {

		public ChrysalisClient(ModContainer container, IEventBus eventBus) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}
}