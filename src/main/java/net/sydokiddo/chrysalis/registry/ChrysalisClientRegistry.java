package net.sydokiddo.chrysalis.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.client.entities.rendering.SeatRenderer;
import net.sydokiddo.chrysalis.registry.entities.registry.ChrysalisEntities;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.util.sounds.music.ClearMusicPayload;
import net.sydokiddo.chrysalis.util.sounds.music.QueuedMusicPayload;
import net.sydokiddo.chrysalis.util.sounds.music.ResetMusicFadePayload;
import net.sydokiddo.chrysalis.util.sounds.music.StructureChangedPayload;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakeHandler;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakeResetPayload;
import net.sydokiddo.chrysalis.util.technical.splash_texts.SplashTextLoader;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import java.io.File;
import java.util.Objects;
import java.util.Random;

public class ChrysalisClientRegistry {

    private static final KeyMapping PANORAMIC_SCREENSHOT_KEY = new KeyMapping("key.chrysalis.panoramic_screenshot", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F6, "key.categories.misc");

    public static void registerAll(FMLClientSetupEvent event) {

        // region Splash Texts

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(SplashTextLoader.INSTANCE);

        // endregion

        // region Packets

        ClientPlayNetworking.registerGlobalReceiver(QueuedMusicPayload.TYPE, (payload, context) -> context.client().execute(() -> setQueuedMusic(new Music(payload.soundEvent(), payload.minDelay(), payload.maxDelay(), payload.replaceCurrentMusic()), true)));
        ClientPlayNetworking.registerGlobalReceiver(StructureChangedPayload.TYPE, (payload, context) -> context.client().execute(() -> setStructureMusic(payload.structureName().toString(), true)));
        ClientPlayNetworking.registerGlobalReceiver(ClearMusicPayload.TYPE, (payload, context) -> context.client().execute(() -> clearMusicOnClient(payload.shouldFade())));
        ClientPlayNetworking.registerGlobalReceiver(ResetMusicFadePayload.TYPE, (payload, context) -> context.client().execute(() -> resetMusicFade = true));
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
        EntityRenderers.register(ChrysalisEntities.SEAT, SeatRenderer::new);

        // endregion
    }

    // region Custom Music

    @Nullable
    private static Music queuedMusic = null;
    public static boolean fadeOutMusic = false;
    public static boolean resetMusicFade = false;

    @Nullable
    public static Music getQueuedMusic() {
        return queuedMusic;
    }

    public static void setQueuedMusic(@Nullable Music music, boolean isFirst) {

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (getQueuedMusic() == music || player != null && EntityDataHelper.getEncounteredMobUUID(player).isPresent()) return;
        queuedMusic = music;

        if (isFirst) {

            int delay = music == null ? new Random().nextInt(6000, 24000) : 100;

            if (ChrysalisMod.IS_DEBUG) {
                ChrysalisMod.LOGGER.info("Set the queued music for the music tracker to {}", music != null ? music.getEvent().getRegisteredName() : null);
                ChrysalisMod.LOGGER.info("Music Delay: {}", delay);
            }

            minecraft.getMusicManager().nextSongDelay = delay;
        }
    }

    public static void setStructureMusic(String structure, boolean isFirst) {
        if (structure == null || Objects.equals(structure, "chrysalis:none")) {
            setQueuedMusic(null, isFirst);
            return;
        }
        setQueuedMusic(ChrysalisSoundEvents.structures.get(structure), isFirst);
    }

    public static void clearMusicOnClient(boolean shouldFade) {
        if (shouldFade) {
            fadeOutMusic = true;
        } else {
            if (getQueuedMusic() != null) Minecraft.getInstance().getSoundManager().stop(getQueuedMusic().getEvent().value().location(), SoundSource.MUSIC);
            setQueuedMusic(null, true);
            setStructureMusic(null, false);
            resetMusicFade = true;
        }
    }

    // endregion
}