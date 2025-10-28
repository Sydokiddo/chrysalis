package net.junebug.chrysalis.mixin.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.storage.LevelSummary;
import net.junebug.chrysalis.Chrysalis;
import net.junebug.chrysalis.common.misc.CGameRules;
import net.junebug.chrysalis.util.helpers.WorldGenHelper;
import net.junebug.chrysalis.util.technical.config.CConfigOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelSummary.class)
public class LevelMixin {

    /**
     * Prevents the experimental warning when starting the world if the user is in a development environment.
     **/

    @Inject(method = "isExperimental", at = @At(value = "HEAD"), cancellable = true)
    private void chrysalis$disableExperimentalWorldWarning(CallbackInfoReturnable<Boolean> cir) {
        if (Chrysalis.IS_DEBUG || !CConfigOptions.EXPERIMENTAL_WORLD_WARNING.get()) cir.setReturnValue(false);
    }

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

    @Mixin(ServerExplosion.class)
    public static class ServerExplosionMixin {

        /**
         * Block-interactive explosions (such as wind charges) are now driven by the mobWorldInteractions game rule rather than the mobGriefing game rule.
         **/

        @ModifyArg(method = "canTriggerBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
        private GameRules.Key<GameRules.BooleanValue> chrysalis$explosionBlockTriggerWorldInteractionsGameRule(GameRules.Key<GameRules.BooleanValue> oldValue) {
            if (!CConfigOptions.REWORKED_MOB_GRIEFING.get()) return oldValue;
            return CGameRules.RULE_MOB_WORLD_INTERACTIONS;
        }
    }
}