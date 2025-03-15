package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes;

import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class MobSightEffect extends MobEffect {

    /**
     * A status effect class that allows for spectator mode shaders to be applied with it.
     **/

    public MobSightEffect(int color) {
        super(MobEffectCategory.NEUTRAL, color);
    }

    @Override
    public void onEffectStarted(@NotNull LivingEntity livingEntity, int amplifier) {
        tryRefreshingPostEffect(livingEntity);
    }

    public static void tryRefreshingPostEffect(LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer serverPlayer) serverPlayer.connection.send(new ClientboundSetCameraPacket(serverPlayer));
    }
}