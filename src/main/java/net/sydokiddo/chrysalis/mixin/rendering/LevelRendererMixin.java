package net.sydokiddo.chrysalis.mixin.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow private @Nullable ClientLevel level;

    @Unique
    private boolean isNether() {
        return this.level != null && this.level.dimension() == Level.NETHER;
    }

    @Unique
    private boolean isEnd() {
        return this.level != null && this.level.dimension() == Level.END;
    }

    // Prevents rain from rendering in the Nether and the End

    /**
     * Prevents rain from being able to render in the Nether and the End.
     * <p>
     * Fixes a vanilla bug where occasionally when going through portals when the world is loading, rain can sometimes display in dimensions where rain doesn't exist.
     **/

    @Inject(method = "renderSnowAndRain", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis_preventRainRenderingInNetherAndEnd(LightTexture lightTexture, float f, double d, double e, double g, CallbackInfo info) {
        if (isNether() || isEnd()) {
            info.cancel();
        }
    }

    @Inject(method = "tickRain", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis_preventRainTickingInNetherAndEnd(Camera camera, CallbackInfo info) {
        if (isNether() || isEnd()) {
            info.cancel();
        }
    }
}