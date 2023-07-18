package net.sydokiddo.chrysalis.mixin.entities.villagers;

import net.minecraft.world.entity.ai.behavior.HarvestFarmland;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HarvestFarmland.class)
public class HarvestFarmlandMixin {

    /**
     * Farmer Villagers harvesting crops from Farmland is now determined by the passiveGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/Villager;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis_villagerPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ModRegistry.RULE_PASSIVE_GRIEFING;
    }
}
