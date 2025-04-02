package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.chrysalis.util.helpers.BlockHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {

    /**
     * Emits an eating sound and particles when a cake is eaten.
     **/

    @Inject(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V"))
    private static void chrysalis$playCakeEatingSound(LevelAccessor level, BlockPos blockPos, BlockState blockState, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        player.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, level.getRandom().triangle(1.0F, 0.2F));
    }

    @Inject(method = "useWithoutItem", at = @At("RETURN"))
    private void chrysalis$emitCakeEatingParticles(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level instanceof ServerLevel serverLevel) BlockHelper.emitDestroyParticlesAtHitPosition(serverLevel, blockState, blockHitResult, 8, 0.0D);
    }
}