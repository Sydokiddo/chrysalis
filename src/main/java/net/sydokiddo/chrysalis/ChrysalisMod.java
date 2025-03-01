package net.sydokiddo.chrysalis;

import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.sydokiddo.chrysalis.registry.ChrysalisClientRegistry;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ChrysalisMod.MOD_ID)
public class ChrysalisMod {

	public static final Logger LOGGER = LoggerFactory.getLogger("ChrysalisMod");
	public static RegistryAccess registryAccess;

	public static final String
		MOD_ID = "chrysalis",
		CHRYSALIS_VERSION = FMLLoader.getLoadingModList().getModFileById(MOD_ID).versionString()
	;

	@SuppressWarnings("unused")
	public static final boolean
		IS_DEBUG = SharedConstants.IS_RUNNING_IN_IDE,
		CHRYSALIS_INITIALIZED = FMLLoader.getLoadingModList().getModFileById(MOD_ID) != null
	;

	public ChrysalisMod(IEventBus modBus) {
		NeoForge.EVENT_BUS.register(this);
		ChrysalisRegistry.registerAll(modBus);
		LOGGER.info("{} v{} has been initialized!", LOGGER.getName(), CHRYSALIS_VERSION);
	}

	@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {

		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			ChrysalisClientRegistry.registerAll(event);
		}
	}

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	public static <T> ResourceKey<Registry<T>> key(String name) {
		return ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace(name));
	}
}