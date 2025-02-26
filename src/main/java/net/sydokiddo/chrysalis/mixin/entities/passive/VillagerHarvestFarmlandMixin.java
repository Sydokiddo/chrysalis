package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.entity.ai.behavior.HarvestFarmland;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HarvestFarmland.class)
public class VillagerHarvestFarmlandMixin {

    /**
     * Farmer villagers harvesting crops from farmland is now determined by the mobWorldInteractions game rule rather than the mobGriefing game rule.
     **/

    @ModifyArg(method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/Villager;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$villagerWorldInteractionsGameRule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS;
    }
}