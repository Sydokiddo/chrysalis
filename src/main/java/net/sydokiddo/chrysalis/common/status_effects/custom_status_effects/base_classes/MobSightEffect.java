package net.sydokiddo.chrysalis.common.status_effects.custom_status_effects.base_classes;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.sydokiddo.chrysalis.util.entities.EntityDataHelper;
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
        if (livingEntity instanceof ServerPlayer serverPlayer) EntityDataHelper.updateCurrentShader(serverPlayer);
    }
}