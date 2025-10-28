package net.junebug.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.junebug.chrysalis.util.helpers.ParticleHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {

    /**
     * Emits an eating sound and particles when a cake is eaten.
     **/

    @Inject(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V", ordinal = 0))
    private static void chrysalis$playCakeEatingSound(LevelAccessor level, BlockPos blockPos, BlockState blockState, Player player, CallbackInfoReturnable<InteractionResult> cir) {

        SoundEvent soundEvent;
        float volume;

        if (blockState.getValue(BlockStateProperties.BITES) >= 6) {
            soundEvent = SoundEvents.PLAYER_BURP;
            volume = 0.5F;
        } else {
            soundEvent = SoundEvents.GENERIC_EAT.value();
            volume = 1.0F;
        }

        player.playSound(soundEvent, volume, level.getRandom().triangle(1.0F, 0.2F));
    }

    @Inject(method = "useWithoutItem", at = @At("RETURN"))
    private void chrysalis$emitCakeEatingParticles(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level instanceof ServerLevel serverLevel) ParticleHelper.emitBlockParticlesAtHitPosition(serverLevel, blockState, blockHitResult, 0.0D, 8, 0.0D);
    }
}