package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = {RemoveBlockGoal.class, Evoker.EvokerWololoSpellGoal.class}, targets = "net/minecraft/world/entity/animal/Rabbit$RaidGardenGoal")
public class MiscGoalsMixin {

    /**
     * Rabbits eating carrot crops and evokers casting their wololo spell is now determined by the passiveGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "canUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$rabbitAndEvokerPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisRegistry.RULE_PASSIVE_GRIEFING;
    }
}