package net.junebug.chrysalis.mixin.entities.hostile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.common.CConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieVillager.class)
public class ZombieVillagerMixin extends Zombie {

    private ZombieVillagerMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Blocks that speed up zombie villager curing while near them is now driven by the speeds_up_zombie_villager_curing tag.
     **/

    @Redirect(method = "getConversionProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean chrysalis$speedsUpZombieVillagerCuringTag(BlockState blockState, Block block) {
        return blockState.is(CTags.SPEEDS_UP_ZOMBIE_VILLAGER_CURING);
    }

    /**
     * Causes the zombie villager to emit particles if it has a cure-speeding block near it.
     **/

    @Inject(method = "getConversionProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F", ordinal = 1))
    private void chrysalis$addConversionProgressSpeedUpParticles(CallbackInfoReturnable<Integer> cir) {
        if (CConfig.TELEGRAPHED_ZOMBIE_VILLAGER_CURING.get() && this.level() instanceof ServerLevel serverLevel) serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }
}