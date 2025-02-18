package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelSummary;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.util.helpers.WorldGenHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelSummary.class)
public class LevelMixin {

    /**
     * Prevents the experimental warning when starting the world if the user is in a development environment.
     **/

    @Inject(method = "isExperimental", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$disableExperimentalWarning(CallbackInfoReturnable<Boolean> cir) {
        if (Chrysalis.IS_DEBUG) cir.setReturnValue(false);
    }

    @SuppressWarnings("unused")
    @Mixin(ServerLevel.class)
    public static abstract class SnowFixMixin {

        @Shadow public abstract ServerLevel getLevel();

        /**
         * Prevents a bug where snow can fall in the nether and end if the y-level can go high enough.
         **/

        @Inject(method = "tickPrecipitation", at = @At(value = "HEAD"), cancellable = true)
        private void chrysalis$preventSnowInNetherAndEnd(BlockPos blockPos, CallbackInfo info) {
            if (WorldGenHelper.isNetherOrEnd(this.getLevel())) info.cancel();
        }
    }
}