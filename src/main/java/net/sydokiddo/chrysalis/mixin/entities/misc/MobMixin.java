package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Mob.class)
public class MobMixin {

    /**
     * Mobs being able to pick up items is now determined by the passiveGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis_mobsPickingUpItemsPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ModRegistry.RULE_PASSIVE_GRIEFING;
    }
}