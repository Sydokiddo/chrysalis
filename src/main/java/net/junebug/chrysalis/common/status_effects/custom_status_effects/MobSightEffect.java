package net.junebug.chrysalis.common.status_effects.custom_status_effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.junebug.chrysalis.util.helpers.EntityHelper;
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
        if (livingEntity instanceof ServerPlayer serverPlayer) EntityHelper.updateCurrentShader(serverPlayer);
    }
}