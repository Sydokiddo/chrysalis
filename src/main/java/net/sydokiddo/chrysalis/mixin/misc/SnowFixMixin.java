package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class SnowFixMixin {

    @Shadow public abstract ServerLevel getLevel();

    @Unique
    private boolean isNether() {
        return this.getLevel() != null && this.getLevel().dimension() == Level.NETHER;
    }

    @Unique
    private boolean isEnd() {
        return this.getLevel() != null && this.getLevel().dimension() == Level.END;
    }

    /**
     * Prevents a bug where snow can fall in the Nether and End if the Y-level can go high enough.
     **/

    @Inject(method = "tickPrecipitation", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$preventSnowInNetherAndEnd(BlockPos blockPos, CallbackInfo info) {
        if (this.isNether() || this.isEnd()) info.cancel();
    }
}