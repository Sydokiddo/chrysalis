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

    // Prevents a bug where snow can fall in the Nether and End

    @Inject(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"), cancellable = true)
    private void chrysalis_preventSnowInNetherAndEnd(LevelChunk levelChunk, int i, CallbackInfo ci) {
        if (isNether() || isEnd()) {
            ci.cancel();
        }
    }
}