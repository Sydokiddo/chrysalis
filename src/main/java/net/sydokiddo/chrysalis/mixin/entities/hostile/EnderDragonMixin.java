package net.sydokiddo.chrysalis.mixin.entities.hostile;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EnderDragon.class)
public class EnderDragonMixin {

    /**
     * Ender dragons being able to destroy blocks is now determined by the dragonGriefing game rule rather than the mobGriefing game rule.
     **/

    @ModifyArg(method = "checkWalls", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$enderDragonGriefingGameRule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisGameRules.RULE_DRAGON_GRIEFING;
    }
}