package net.sydokiddo.chrysalis.mixin.testing;

import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.client.particles.options.RotatingDustParticleOptions;
import net.sydokiddo.chrysalis.client.particles.options.SmallPulsationParticleOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.awt.*;

@OnlyIn(Dist.CLIENT)
@Mixin(LevelEventHandler.class)
public class LevelEventHandlerMixin {

    @Shadow @Final private Level level;

    @Inject(method = "levelEvent", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$replaceLevelEvents(int levelEvent, BlockPos blockPos, int direction, CallbackInfo info) {
        if (levelEvent == 1503) {

            info.cancel();

            this.level.playLocalSound(blockPos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);

            int color = Color.decode("#6AC1A4").getRGB();
            this.level.addParticle(new SmallPulsationParticleOptions(color, false, Direction.UP.get3DDataValue(), 10), blockPos.getCenter().x(), blockPos.getY() + 1.0D, blockPos.getCenter().z(), 0.0D, 0.0D, 0.0D);

            for (int particleAmount = 0; particleAmount < 16; particleAmount++) {
                double random = (5.0D + this.level.getRandom().nextDouble() * 6.0D) / 16.0D;
                double x = (double) blockPos.getX() + random;
                double y = (double) blockPos.getY() + 0.8D;
                double z = (double) blockPos.getZ() + random;
                this.level.addParticle(new RotatingDustParticleOptions(color, true, true, false, 1.0F), x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}