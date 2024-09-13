package net.sydokiddo.chrysalis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.sounds.Music;
import net.sydokiddo.chrysalis.misc.util.music.StructureChangedPayload;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ChrysalisClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StructureChangedPayload.TYPE, ((payload, context) -> context.client().execute(() -> setStructureMusic(payload.structureName().toString()))));
    }

    @Nullable public static Music structureMusic = null;

    @Nullable
    public static Music getStructureMusic() {
        return structureMusic;
    }

    public static void setStructureMusic(String structure) {
        if (structure == null || Objects.equals(structure, "chrysalis:none")) {
            structureMusic = null;
            return;
        }
        structureMusic = ChrysalisSoundEvents.structures.get(structure);
    }
}