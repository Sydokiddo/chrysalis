package net.sydokiddo.chrysalis.mixin.testing;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sydokiddo.chrysalis.client.particles.options.RotatingDustParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.awt.*;

@Mixin(EndPortalBlock.class)
public class EndPortalParticleTestingMixin {

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void chrysalis$changeEndPortalParticles(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource, CallbackInfo info) {

        info.cancel();

        double x = (double) blockPos.getX() + randomSource.nextDouble();
        double y = (double) blockPos.getY() + 0.8D;
        double z = (double) blockPos.getZ() + randomSource.nextDouble();

        level.addParticle(new RotatingDustParticleOptions(Color.decode("#6AC1A4").getRGB(), true, true, false, 1.0F), x, y, z, 0.0, 0.0, 0.0);
    }
}