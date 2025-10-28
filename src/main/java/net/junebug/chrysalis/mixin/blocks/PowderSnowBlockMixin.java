package net.junebug.chrysalis.mixin.blocks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.junebug.chrysalis.common.misc.CGameRules;
import net.junebug.chrysalis.common.misc.CTags;
import net.junebug.chrysalis.util.technical.config.CConfigOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    /**
     * Mobs that are on fire and destroy powder snow when going into it is now determined by the mobWorldInteractions game rule rather than the mobGriefing game rule.
     **/

    @ModifyArg(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$powderSnowWorldInteractionsGameRule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        if (!CConfigOptions.REWORKED_MOB_GRIEFING.get()) return oldValue;
        return CGameRules.RULE_MOB_WORLD_INTERACTIONS;
    }

    /**
     * Any items in the powder_snow_walkable_items tag that can be equipped in the feet slot will allow for the user to walk on powder snow.
     **/

    @Inject(method = "canEntityWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
    private static void chrysalis$powderSnowWalkableItemsTag(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.FEET).is(CTags.POWDER_SNOW_WALKABLE_ITEMS)) cir.setReturnValue(true);
    }
}