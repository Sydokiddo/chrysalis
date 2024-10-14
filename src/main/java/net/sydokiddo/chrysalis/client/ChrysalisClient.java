package net.sydokiddo.chrysalis.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.Music;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakeHandler;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakeResetPayload;
import net.sydokiddo.chrysalis.misc.util.music.StructureChangedPayload;
import net.sydokiddo.chrysalis.misc.util.splash_texts.SplashTextLoader;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import java.io.File;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ChrysalisClient implements ClientModInitializer {

    private static final KeyMapping PANORAMIC_SCREENSHOT_KEY = new KeyMapping("key.chrysalis.panoramic_screenshot", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F6, "key.categories.misc");

    @Override
    public void onInitializeClient() {

        // region Splash Texts

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(SplashTextLoader.INSTANCE);

        // endregion

        // region Packets

        ClientPlayNetworking.registerGlobalReceiver(StructureChangedPayload.TYPE, ((payload, context) -> context.client().execute(() -> setStructureMusic(payload.structureName().toString()))));
        ClientPlayNetworking.registerGlobalReceiver(CameraShakePayload.TYPE, (payload, context) -> context.client().execute(() -> CameraShakeHandler.shakeCamera(payload.time(), payload.strength(), payload.frequency())));
        ClientPlayNetworking.registerGlobalReceiver(CameraShakeResetPayload.TYPE, (payload, context) -> context.client().execute(CameraShakeHandler::resetCamera));

        // endregion

        // region Panoramic Screenshots

        KeyBindingHelper.registerKeyBinding(PANORAMIC_SCREENSHOT_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (PANORAMIC_SCREENSHOT_KEY.consumeClick()) {

                client.grabPanoramixScreenshot(new File(FabricLoader.getInstance().getGameDir().toString()), 1024, 1024);

                Component panoramaTakenText = Component.literal("panorama_0.png – panorama_5.png").withStyle(ChatFormatting.UNDERLINE)
                .withStyle((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, new File(FabricLoader.getInstance().getGameDir().toString() + "/screenshots/").getAbsolutePath())));

                Component message = Component.translatable("screenshot.success", panoramaTakenText);
                client.gui.getChat().addMessage(message);
                client.getNarrator().sayNow(message);
            }
        });

        // endregion
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