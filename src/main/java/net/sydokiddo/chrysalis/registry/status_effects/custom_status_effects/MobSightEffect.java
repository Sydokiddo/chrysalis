package net.sydokiddo.chrysalis.registry.status_effects.custom_status_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MobSightEffect extends GenericStatusEffect {

    /**
     * A status effect class that allows for spectator mode shaders to be applied with it.
     **/

    @SuppressWarnings("unused")
    public MobSightEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    public MobSightEffect(MobEffectCategory mobEffectCategory, int color, ParticleOptions particleType) {
        super(mobEffectCategory, color, particleType);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        tryRefreshingPostEffect(livingEntity);
    }

    public static void tryRefreshingPostEffect(LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer serverPlayer) serverPlayer.connection.send(new ClientboundSetCameraPacket(serverPlayer));
    }
}