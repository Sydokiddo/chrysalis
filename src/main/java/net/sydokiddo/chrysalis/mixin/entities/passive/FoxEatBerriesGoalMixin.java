package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Fox.FoxEatBerriesGoal.class)
public class FoxEatBerriesGoalMixin {

    /**
     * Foxes being able to eat sweet berries or glow berries is now determined by the mobWorldInteractions game rule rather than the mobGriefing game rule.
     **/

    @ModifyArg(method = "onReachedTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$foxWorldInteractionsGameRule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS;
    }
}