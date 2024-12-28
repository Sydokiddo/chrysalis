package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.misc.util.helpers.WorldGenHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WeatherEffectRenderer.class)
public class WeatherFixMixin {

    /**
     * Fixes a vanilla bug where occasionally when going through portals when the world is loading, rain can sometimes display in dimensions where rain doesn't exist.
     **/

    @Inject(method = "render(Lnet/minecraft/world/level/Level;Lnet/minecraft/client/renderer/MultiBufferSource;IFLnet/minecraft/world/phys/Vec3;)V", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$preventWeatherRenderingInNetherAndEnd(Level level, MultiBufferSource multiBufferSource, int int1, float float1, Vec3 vec3, CallbackInfo info) {
        if (WorldGenHelper.isNetherOrEnd(level)) info.cancel();
    }

    @Inject(method = "tickRainParticles", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$preventWeatherTickingInNetherAndEnd(ClientLevel clientLevel, Camera camera, int int1, ParticleStatus particleStatus, CallbackInfo info) {
        if (WorldGenHelper.isNetherOrEnd(clientLevel)) info.cancel();
    }

    @SuppressWarnings("unused")
    @Mixin(ServerLevel.class)
    public static abstract class SnowFixMixin {

        @Shadow public abstract ServerLevel getLevel();

        /**
         * Prevents a bug where snow can fall in the Nether and End if the Y-level can go high enough.
         **/

        @Inject(method = "tickPrecipitation", at = @At(value = "HEAD"), cancellable = true)
        private void chrysalis$preventSnowInNetherAndEnd(BlockPos blockPos, CallbackInfo info) {
            if (WorldGenHelper.isNetherOrEnd(this.getLevel())) info.cancel();
        }
    }
}