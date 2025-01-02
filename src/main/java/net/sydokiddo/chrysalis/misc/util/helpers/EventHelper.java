package net.sydokiddo.chrysalis.misc.util.helpers;

import com.mojang.blaze3d.shaders.FogShape;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakeResetPayload;
import net.sydokiddo.chrysalis.misc.util.entities.EncounterMusicMob;
import net.sydokiddo.chrysalis.misc.util.entities.EntityDataHelper;
import net.sydokiddo.chrysalis.misc.util.music.ClearMusicPayload;
import net.sydokiddo.chrysalis.misc.util.music.QueuedMusicPayload;
import org.joml.Vector4f;
import java.util.List;

@SuppressWarnings("unused")
public class EventHelper {

    /**
     * Gets any nearby players within a specific range of a selected entity's hitbox.
     **/

    public static List<? extends ServerPlayer> getNearbyPlayers(Entity entity, double range) {
        return entity.level().getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(range), EntitySelector.NO_SPECTATORS);
    }

    /**
     * Sends a customizable camera shake effect to a selected player.
     **/

    public static void sendCameraShake(ServerPlayer serverPlayer, int time, int strength, int frequency) {
        ServerPlayNetworking.send(serverPlayer, new CameraShakePayload(time, strength, frequency));
    }

    public static void sendCameraShakeToNearbyPlayers(Entity entity, Entity ignoredEntity, double range, int time, int strength, int frequency) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            sendCameraShake(serverPlayer, time, strength, frequency);
        }
    }

    public static void resetCameraShake(ServerPlayer serverPlayer) {
        ServerPlayNetworking.send(serverPlayer, new CameraShakeResetPayload(1));
    }

    public static void resetCameraShakeForNearbyPlayers(Entity entity, Entity ignoredEntity, double range) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            resetCameraShake(serverPlayer);
        }
    }

    /**
     * Sends a specific status effect to nearby players.
     **/

    public static void sendStatusEffectToNearbyPlayers(Entity entity, Entity ignoredEntity, double range, MobEffectInstance mobEffectInstance) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            serverPlayer.addEffect(mobEffectInstance);
        }
    }

    /**
     * Sends a queued music track to a selected player.
     **/

    public static void sendMusic(ServerPlayer serverPlayer, Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        ServerPlayNetworking.send(serverPlayer, new QueuedMusicPayload(soundEvent, minDelay, maxDelay, replaceCurrentMusic));
    }

    public static void sendMusicToNearbyPlayers(Entity entity, Entity ignoredEntity, double range, Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            sendMusic(serverPlayer, soundEvent, minDelay, maxDelay, replaceCurrentMusic);
        }
    }

    public static void sendEncounterMusic(Mob mob, Holder<SoundEvent> soundEvent, boolean playOnFirstTick) {

        if (!(mob instanceof EncounterMusicMob encounterMusicMob) || !playOnFirstTick && !checkEncounterMusicRefreshing(mob)) return;

        for (ServerPlayer serverPlayer : getNearbyPlayers(mob, encounterMusicMob.chrysalis$getFinalEncounterMusicRange())) {
            if (EntityDataHelper.getEncounteredMobUUID(serverPlayer).isPresent()) return;
            EntityDataHelper.setEncounteredMobUUID(serverPlayer, mob.getUUID());
            sendMusic(serverPlayer, soundEvent, 10, 10, true);
        }
    }

    private static int ticksWithinRange = 0;

    private static boolean checkEncounterMusicRefreshing(Mob mob) {

        if (!(mob instanceof EncounterMusicMob encounterMusicMob)) return false;
        List<? extends ServerPlayer> nearbyPlayers = getNearbyPlayers(mob, encounterMusicMob.chrysalis$getFinalEncounterMusicRange());

        if (nearbyPlayers.isEmpty()) {
            ticksWithinRange = 0;
            return false;
        } else {
            ++ticksWithinRange;
            if (ticksWithinRange < 100) ++ticksWithinRange;
            else return mob.tickCount % 20 == 0;
        }

        return false;
    }

    public static void clearAllMusic(ServerPlayer serverPlayer) {
        ServerPlayNetworking.send(serverPlayer, new ClearMusicPayload(true, SoundEvents.MUSIC_GAME));
    }

    public static void clearAllMusicForNearbyPlayers(Entity entity, Entity ignoredEntity, double range) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            clearAllMusic(serverPlayer);
        }
    }

    public static void clearSpecificMusic(ServerPlayer serverPlayer, Holder<SoundEvent> soundEvent) {
        ServerPlayNetworking.send(serverPlayer, new ClearMusicPayload(false, soundEvent));
    }

    public static void clearSpecificMusicForNearbyPlayers(Entity entity, Entity ignoredEntity, double range, Holder<SoundEvent> soundEvent) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            clearSpecificMusic(serverPlayer, soundEvent);
        }
    }

    /**
     * Assists with creating a custom fog effect.
     **/

    public static FogParameters createCustomFog(float start, float end, FogShape fogShape, Vector4f vector4f) {
        return new FogParameters(start, end, fogShape, vector4f.x(), vector4f.y(), vector4f.z(), vector4f.w());
    }
}