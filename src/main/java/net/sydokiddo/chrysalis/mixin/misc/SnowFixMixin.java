package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
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

    @Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tickPrecipitation(Lnet/minecraft/core/BlockPos;)V"), cancellable = true)
    private void chrysalis$preventSnowInNetherAndEnd(LevelChunk levelChunk, int i, CallbackInfo ci) {
        if (isNether() || isEnd()) {
            ci.cancel();
        }
    }
}