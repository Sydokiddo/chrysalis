package net.sydokiddo.chrysalis.mixin.entities.animals.fox;

import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Fox.FoxEatBerriesGoal.class)
public class FoxEatBerriesGoalMixin {

    /**
     * Foxes being able to eat Sweet Berries or Glow Berries is now determined by the passiveGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "onReachedTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis_foxPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ModRegistry.RULE_PASSIVE_GRIEFING;
    }
}
