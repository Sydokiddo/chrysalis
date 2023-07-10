package net.sydokiddo.chrysalis.mixin.blocks.powder_snow;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    // Mobs that are on fire destroying Powder Snow is now determined by the passiveGriefing gamerule rather than mobGriefing

    @ModifyArg(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis_powderSnowPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ModRegistry.RULE_PASSIVE_GRIEFING;
    }
}
