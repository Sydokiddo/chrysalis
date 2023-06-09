package net.sydokiddo.chrysalis.mixin.entities.bosses.ender_dragon;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EnderDragon.class)
public class EnderDragonMixin {

    // Ender Dragons destroying blocks is now determined by the dragonGriefing gamerule rather than mobGriefing

    @ModifyArg(method = "checkWalls", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis_enderDragonGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ModRegistry.RULE_DRAGON_GRIEFING;
    }
}
