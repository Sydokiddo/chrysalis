package net.sydokiddo.chrysalis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisResourcePacks;

@Environment(EnvType.CLIENT)
public class ChrysalisClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ChrysalisResourcePacks.registerResourcePacks();
    }
}