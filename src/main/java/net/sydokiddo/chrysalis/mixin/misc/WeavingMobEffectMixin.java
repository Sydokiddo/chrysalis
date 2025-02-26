package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.world.effect.WeavingMobEffect;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WeavingMobEffect.class)
public class WeavingMobEffectMixin {

    /**
     * Cobwebs being placed by mobs with the weaving status effect is now determined by the mobWorldInteractions game rule rather than the mobGriefing game rule.
     **/

    @ModifyArg(method = "onMobRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$weavingWorldInteractionsGameRule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS;
    }
}