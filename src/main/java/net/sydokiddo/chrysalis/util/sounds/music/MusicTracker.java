package net.sydokiddo.chrysalis.util.sounds.music;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.common.misc.ChrysalisSoundEvents;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.util.sounds.music.payloads.StructureChangedPayload;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class MusicTracker {

    @Nullable private static Music queuedMusic = null;
    public static boolean fadeOutMusic = false;
    public static boolean resetMusicFade = false;

    /**
     * Gets the music tracker's current queued music.
     **/

    @Nullable
    public static Music getQueuedMusic() {
        return queuedMusic;
    }

    /**
     * Sets the music tracker's queued music to a specific music.
     **/

    public static void setQueuedMusic(@Nullable Music music, boolean isFirst) {

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (getQueuedMusic() == music || player != null && EntityDataHelper.getEncounteredMobUUID(player).isPresent()) return;

        queuedMusic = music;

        if (isFirst) {

            int delay = music == null ? new Random().nextInt(6000, 24000) : 100;

            if (Chrysalis.IS_DEBUG) {
                Chrysalis.LOGGER.info("Set the queued music for the music tracker to {}", music != null ? music.getEvent().getRegisteredName() : null);
                Chrysalis.LOGGER.info("Music Delay: {}", delay);
            }

            minecraft.getMusicManager().nextSongDelay = delay;
        }
    }

    /**
     * Clears the currently playing music track on the client.
     **/

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

    // region Structure Music

    public static Map<Player, ResourceLocation> playerStructures = new HashMap<>();
    public static int ticks = 0;

    /**
     * Changes the current structure resourceLocationId based on the player's location.
     **/

    private static void setCurrentStructure(Player player, Level level, String structureLocation) {

        @Nullable ResourceLocation oldStructure = playerStructures.get(player);
        ResourceLocation newStructure = Chrysalis.resourceLocationId("none");

        if (!Objects.equals(structureLocation, null)) newStructure = ResourceLocation.parse(structureLocation);
        if (Objects.equals(oldStructure, newStructure)) return;

        playerStructures.put(player, newStructure);

        if (level.isClientSide()) {
            MusicTracker.setStructureMusic(structureLocation, true);
            return;
        }

        if (Chrysalis.IS_DEBUG) Chrysalis.LOGGER.info("Setting {}'s current structure to {}", player.getName().getString(), structureLocation);
        if (player instanceof ServerPlayer serverPlayer) PacketDistributor.sendToPlayer(serverPlayer, new StructureChangedPayload(newStructure));
    }

    /**
     * Checks all registered structures tied to music tracks, and checks if the player is within the bounds of one.
     **/

    public static void checkAllStructures(ServerLevel serverLevel, Player player) {

        boolean structureFound = false;

        for (Holder<Structure> structure : serverLevel.structureManager().registryAccess().lookupOrThrow(Registries.STRUCTURE).asHolderIdMap()) {
            if (serverLevel.structureManager().getStructureWithPieceAt(BlockPos.containing(player.position()), structure.value()).isValid()) {
                setCurrentStructure(player, serverLevel, structure.getRegisteredName());
                structureFound = true;
                break;
            }
        }

        if (!structureFound) setCurrentStructure(player, serverLevel, null);
    }

    /**
     * Sets the queued music to the specific structure's one.
     **/

    public static void setStructureMusic(String structure, boolean isFirst) {
        if (structure == null || Objects.equals(structure, Chrysalis.stringId("none"))) {
            setQueuedMusic(null, isFirst);
            return;
        }
        setQueuedMusic(ChrysalisSoundEvents.structures.get(structure), isFirst);
    }

    // endregion
}