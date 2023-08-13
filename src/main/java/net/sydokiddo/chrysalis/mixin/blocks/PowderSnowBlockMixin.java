package net.sydokiddo.chrysalis.mixin.blocks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    /**
     * Mobs that are on fire and destroy Powder Snow when going into it is now determined by the passiveGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis_powderSnowPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ModRegistry.RULE_PASSIVE_GRIEFING;
    }

    /**
     * Any items in the powder_snow_walkable_items tag that can be equipped in the feet slot will allow the user to walk on Powder Snow.
     **/

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
    private static void chrysalis_powderSnowWalkableItemsTag(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.FEET).is(ChrysalisTags.POWDER_SNOW_WALKABLE_ITEMS)) {
            cir.setReturnValue(true);
        }
    }
}