package net.sydokiddo.chrysalis.util.helpers;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.sydokiddo.chrysalis.common.misc.CSoundEvents;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.util.technical.camera.CameraShakeResetPayload;
import net.sydokiddo.chrysalis.util.entities.interfaces.EncounterMusicMob;
import net.sydokiddo.chrysalis.util.sounds.music.payloads.ClearMusicPayload;
import net.sydokiddo.chrysalis.util.sounds.music.payloads.QueuedMusicPayload;
import net.sydokiddo.chrysalis.util.sounds.music.payloads.ResetMusicFadePayload;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
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

    // region Camera Shaking

    /**
     * Sends a customizable camera shake effect to a selected player.
     **/

    public static void sendCameraShake(ServerPlayer serverPlayer, int time, int strength, int frequency) {
        PacketDistributor.sendToPlayer(serverPlayer, new CameraShakePayload(time, strength, frequency));
    }

    public static void sendCameraShakeToNearbyPlayers(Entity entity, Entity ignoredEntity, double range, int time, int strength, int frequency) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            sendCameraShake(serverPlayer, time, strength, frequency);
        }
    }

    public static void resetCameraShake(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer(serverPlayer, new CameraShakeResetPayload(1));
    }

    public static void resetCameraShakeForNearbyPlayers(Entity entity, Entity ignoredEntity, double range) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            resetCameraShake(serverPlayer);
        }
    }

    // endregion

    // region Status Effects

    /**
     * Sends a specific status effect to nearby players.
     **/

    public static void sendStatusEffectToNearbyPlayers(Entity entity, Entity ignoredEntity, double range, MobEffectInstance mobEffectInstance) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            serverPlayer.addEffect(mobEffectInstance);
        }
    }

    // endregion

    // region Custom Music

    /**
     * Sends a queued music track to a selected player.
     **/

    public static void sendMusic(ServerPlayer serverPlayer, Holder<SoundEvent> soundEvent, int minDelay, int maxDelay, boolean replaceCurrentMusic) {
        PacketDistributor.sendToPlayer(serverPlayer, new QueuedMusicPayload(soundEvent, minDelay, maxDelay, replaceCurrentMusic));
        if (replaceCurrentMusic) resetMusicFade(serverPlayer);
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
            if (EntityHelper.getEncounteredMobUUID(serverPlayer).isPresent()) return;
            EntityHelper.setEncounteredMobUUID(serverPlayer, mob.getUUID());
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

    public static void clearMusicOnServer(ServerPlayer serverPlayer, boolean shouldFade) {
        PacketDistributor.sendToPlayer(serverPlayer, new ClearMusicPayload(shouldFade));
        if (!shouldFade) resetMusicFade(serverPlayer);
    }

    public static void clearMusicForNearbyPlayers(Entity entity, Entity ignoredEntity, double range, boolean shouldFade) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            clearMusicOnServer(serverPlayer, shouldFade);
        }
    }

    public static void resetMusicFade(ServerPlayer serverPlayer) {
        PacketDistributor.sendToPlayer(serverPlayer, new ResetMusicFadePayload(0));
    }

    public static void resetMusicFadeForNearbyPlayers(Entity entity, Entity ignoredEntity, double range) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            resetMusicFade(serverPlayer);
        }
    }

    // endregion

    // region Custom Fog

    /**
     * Assists with creating a custom fog effect.
     **/

    public static FogParameters createCustomFog(float start, float end, FogShape fogShape, Vector4f vector4f) {
        return new FogParameters(start, end, fogShape, vector4f.x(), vector4f.y(), vector4f.z(), vector4f.w());
    }

    // endregion

    // region Miscellaneous

    /**
     * UI sound events.
     **/

    public static void playItemDroppingSound(Player player) {
        if (CConfigOptions.ITEM_DROPPING_SOUND.get()) player.playNotifySound(CSoundEvents.ITEM_DROP.get(), player.getSoundSource(), 0.2F, 0.5F + player.level().getRandom().nextFloat() * 0.5F);
    }

    @OnlyIn(Dist.CLIENT)
    public static void playUIClickSound(Minecraft minecraft) {
        playUIClickSound(minecraft, SoundEvents.UI_BUTTON_CLICK);
    }

    @OnlyIn(Dist.CLIENT)
    public static void playUIClickSound(Minecraft minecraft, Holder<SoundEvent> soundEvent) {
        playUIClickSound(minecraft, soundEvent, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static void playUIClickSound(Minecraft minecraft, Holder<SoundEvent> soundEvent, float pitch) {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(soundEvent, pitch));
    }

    // endregion
}