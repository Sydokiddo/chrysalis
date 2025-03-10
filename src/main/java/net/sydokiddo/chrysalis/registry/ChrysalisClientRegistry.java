package net.sydokiddo.chrysalis.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.sydokiddo.chrysalis.ChrysalisMod;
import net.sydokiddo.chrysalis.client.entities.rendering.SeatRenderer;
import net.sydokiddo.chrysalis.registry.entities.registry.ChrysalisEntities;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisParticles;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import java.util.Objects;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ChrysalisClientRegistry {

    public static final KeyMapping PANORAMIC_SCREENSHOT_KEY = new KeyMapping("key.chrysalis.panoramic_screenshot", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F6, "key.categories.misc");

    @SuppressWarnings("unused")
    public static void registerAll(FMLClientSetupEvent event) {

        IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();

        if (modBus != null) {
            modBus.addListener(ChrysalisParticles::registerClient);
        }

        EntityRenderers.register(ChrysalisEntities.SEAT.get(), SeatRenderer::new); // Works
    }

    // region Custom Music

    @Nullable private static Music queuedMusic = null;
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