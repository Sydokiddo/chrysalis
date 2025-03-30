package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.common.misc.CSoundEvents;
import net.sydokiddo.chrysalis.util.technical.config.CConfigOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import static net.minecraft.world.item.Item.getPlayerPOVHitResult;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {

    /**
     * Plays a sound and displays particles when a spawn egg is used on a spawner block.
     **/

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V", ordinal = 0))
    private void chrysalis$playSpawnerChangeEntitySound(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        BlockPos blockPos = useOnContext.getClickedPos();
        useOnContext.getLevel().playSound(null, blockPos, CSoundEvents.GENERIC_SPAWNER_CHANGE_ENTITY.get(), SoundSource.BLOCKS);
        if (useOnContext.getLevel() instanceof ServerLevel serverLevel) for (int amount = 0; amount < 8; ++amount) serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, blockPos.getCenter().x(), blockPos.getY() + 1, blockPos.getCenter().z(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    /**
     * Spawn eggs now play a unique sound when used.
     **/

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;)V", ordinal = 1))
    private void chrysalis$playSpawnEggUseOnSound(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        this.chrysalis$playSpawnEggSound(useOnContext.getLevel(), useOnContext.getClickedPos());
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/world/phys/Vec3;)V"))
    private void chrysalis$playSpawnEggUseSound(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        this.chrysalis$playSpawnEggSound(level, getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY).getBlockPos());
    }

    @Unique
    private void chrysalis$playSpawnEggSound(Level level, BlockPos blockPos) {
        if (CConfigOptions.SPAWN_EGG_USE_SOUND.get()) level.playSound(null, blockPos, CSoundEvents.SPAWN_EGG_USE.get(), SoundSource.NEUTRAL);
    }
}