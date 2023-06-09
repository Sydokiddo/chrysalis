package net.sydokiddo.chrysalis.registry.misc;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.Chrysalis;

public class ModResourcePacks {

    // Registry for Resource Packs:

    @SuppressWarnings("ALL")
    public static void registerResourcePacks() {

        ResourceLocation emissivePack = new ResourceLocation(Chrysalis.MOD_ID, "emissive_textures");

        FabricLoader.getInstance().getModContainer(Chrysalis.MOD_ID).ifPresent(container
        -> ResourceManagerHelper.registerBuiltinResourcePack(emissivePack, container,
        Component.translatable("pack.chrysalis.emissive_textures"), ResourcePackActivationType.ALWAYS_ENABLED));
    }
}
