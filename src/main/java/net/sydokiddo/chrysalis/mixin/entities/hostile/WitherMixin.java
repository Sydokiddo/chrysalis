package net.sydokiddo.chrysalis.mixin.entities.hostile;

import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WitherBoss.class)
public class WitherMixin {

    /**
     * Withers being able to destroy blocks is now determined by the witherGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis_witherGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ModRegistry.RULE_WITHER_GRIEFING;
    }
}