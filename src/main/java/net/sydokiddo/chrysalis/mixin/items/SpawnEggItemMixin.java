package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.sydokiddo.chrysalis.common.misc.ChrysalisSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V", ordinal = 0))
    private void chrysalis$playSpawnerChangeEntitySound(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        BlockPos blockPos = useOnContext.getClickedPos();
        useOnContext.getLevel().playSound(null, blockPos, ChrysalisSoundEvents.GENERIC_SPAWNER_CHANGE_ENTITY.get(), SoundSource.BLOCKS);
        if (useOnContext.getLevel() instanceof ServerLevel serverLevel) for (int amount = 0; amount < 8; ++amount) serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, blockPos.getCenter().x(), blockPos.getY() + 1, blockPos.getCenter().z(), 1, 0.0, 0.0, 0.0, 0.0);
    }
}