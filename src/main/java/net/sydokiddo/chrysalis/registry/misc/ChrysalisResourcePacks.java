package net.sydokiddo.chrysalis.registry.misc;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.Chrysalis;

public class ChrysalisResourcePacks {

    public static void registerResourcePacks() {

        ResourceLocation emissivePack = Chrysalis.id("emissive_textures");

        FabricLoader.getInstance().getModContainer(Chrysalis.MOD_ID).ifPresent(container
        -> ResourceManagerHelper.registerBuiltinResourcePack(emissivePack, container,
        Component.translatable("pack.chrysalis.emissive_textures"), ResourcePackActivationType.ALWAYS_ENABLED));
    }
}