package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Fox.FoxEatBerriesGoal.class)
public class FoxEatBerriesGoalMixin {

    /**
     * Foxes being able to eat sweet berries or glow berries is now determined by the passiveGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "onReachedTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$foxPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisRegistry.RULE_PASSIVE_GRIEFING;
    }
}