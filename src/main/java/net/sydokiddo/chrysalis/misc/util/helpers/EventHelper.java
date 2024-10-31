package net.sydokiddo.chrysalis.misc.util.helpers;

import com.mojang.blaze3d.shaders.FogShape;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakePayload;
import net.sydokiddo.chrysalis.misc.util.camera.CameraShakeResetPayload;
import org.joml.Vector4f;
import java.util.List;

@SuppressWarnings("unused")
public class EventHelper {

    /**
     * Gets any nearby players within a specific range of a selected entity's hitbox.
     **/

    public static List<? extends ServerPlayer> getNearbyPlayers(Entity entity, float range) {
        return entity.level().getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(range), EntitySelector.NO_SPECTATORS);
    }

    /**
     * Sends a customizable camera shake effect to a selected player.
     **/

    public static void sendCameraShake(ServerPlayer serverPlayer, int time, int strength, int frequency) {
        ServerPlayNetworking.send(serverPlayer, new CameraShakePayload(time, strength, frequency));
    }

    public static void sendCameraShakeToNearbyPlayers(Entity entity, Entity ignoredEntity, float range, int time, int strength, int frequency) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            sendCameraShake(serverPlayer, time, strength, frequency);
        }
    }

    public static void resetCameraShake(ServerPlayer serverPlayer) {
        ServerPlayNetworking.send(serverPlayer, new CameraShakeResetPayload(1));
    }

    public static void resetCameraShakeForNearbyPlayers(Entity entity, Entity ignoredEntity, float range) {
        for (ServerPlayer serverPlayer : getNearbyPlayers(entity, range)) {
            if (serverPlayer == ignoredEntity) return;
            resetCameraShake(serverPlayer);
        }
    }

    /**
     * Assists with creating a custom fog effect.
     **/

    public static FogParameters createCustomFog(float start, float end, FogShape fogShape, Vector4f vector4f) {
        return new FogParameters(start, end, fogShape, vector4f.x(), vector4f.y(), vector4f.z(), vector4f.w());
    }
}