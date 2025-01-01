package net.sydokiddo.chrysalis.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.client.entities.rendering.SeatRenderer;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakeHandler;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakeResetPayload;
import net.sydokiddo.chrysalis.misc.util.music.QueuedMusicPayload;
import net.sydokiddo.chrysalis.misc.util.music.ClearMusicPayload;
import net.sydokiddo.chrysalis.misc.util.music.StructureChangedPayload;
import net.sydokiddo.chrysalis.misc.util.splash_texts.SplashTextLoader;
import net.sydokiddo.chrysalis.registry.entities.registry.ChrysalisEntities;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
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

        ClientPlayNetworking.registerGlobalReceiver(QueuedMusicPayload.TYPE, (payload, context) -> context.client().execute(() -> setQueuedMusic(new Music(payload.soundEvent(), payload.minDelay(), payload.maxDelay(), payload.replaceCurrentMusic()))));
        ClientPlayNetworking.registerGlobalReceiver(StructureChangedPayload.TYPE, (payload, context) -> context.client().execute(() -> setStructureMusic(payload.structureName().toString())));

        ClientPlayNetworking.registerGlobalReceiver(ClearMusicPayload.TYPE, (payload, context) -> context.client().execute(() -> {
            if (payload.clearAll()) clearAllMusic(true);
            else clearSpecificMusic(payload.soundEvent());
        }));

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

        // region Miscellaneous

        ChrysalisParticles.registerParticles();
        EntityRendererRegistry.register(ChrysalisEntities.SEAT, SeatRenderer::new);

        // endregion
    }

    // region Custom Music

    @Nullable private static Music queuedMusic = null;

    @Nullable
    public static Music getQueuedMusic() {
        return queuedMusic;
    }

    public static void setQueuedMusic(@Nullable Music music) {
        queuedMusic = music;
    }

    public static void setStructureMusic(String structure) {
        if (structure == null || Objects.equals(structure, "chrysalis:none")) {
            queuedMusic = null;
            return;
        }
        if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Setting current structure to {} for the music tracker", structure);
        queuedMusic = ChrysalisSoundEvents.structures.get(structure);
    }

    public static void clearAllMusic(boolean stopMusicTracker) {
        if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Clearing all music from the music tracker");
        if (stopMusicTracker) Minecraft.getInstance().getMusicManager().stopPlaying();
        setQueuedMusic(null);
        setStructureMusic(null);
    }

    public static void clearSpecificMusic(Holder<SoundEvent> soundEvent) {
        if (getQueuedMusic() != null && getQueuedMusic().getEvent() == soundEvent) {
            ResourceLocation soundEventLocation = soundEvent.value().location();
            if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Clearing music event {} from the music tracker", soundEventLocation);
            Minecraft.getInstance().getSoundManager().stop(soundEventLocation, SoundSource.MUSIC);
            setQueuedMusic(null);
        }
    }

    // endregion
}