package net.sydokiddo.chrysalis.mixin.entities.hostile;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EnderDragon.class)
public class EnderDragonMixin {

    /**
     * Ender dragons being able to destroy blocks is now determined by the dragonGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "checkWalls", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$enderDragonGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisRegistry.RULE_DRAGON_GRIEFING;
    }
}