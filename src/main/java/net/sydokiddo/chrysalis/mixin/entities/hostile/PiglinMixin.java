package net.sydokiddo.chrysalis.mixin.entities.hostile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisGameRules;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Piglin.class)
public class PiglinMixin {

    /**
     * Piglins being able to pick up items is now determined by the mobWorldInteractions game rule rather than the mobGriefing game rule.
     **/

    @ModifyArg(method = "wantsToPickUp", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$piglinWorldInteractionsGameRule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisGameRules.RULE_MOB_WORLD_INTERACTIONS;
    }

    @SuppressWarnings("unused")
    @Mixin(PiglinAi.class)
    public static class PiglinAiMixin {

        /**
         * Items that can be bartered to piglins is now determined by the piglin_bartering_items tag.
         **/

        @Inject(at = @At("HEAD"), method = "isBarterCurrency", cancellable = true)
        private static void chrysalis$piglinBarteringItemsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
            if (itemStack.is(ChrysalisTags.PIGLIN_BARTERING_ITEMS)) cir.setReturnValue(true);
        }

        /**
         * Piglins will run away from any entity in the piglin_avoided tag.
         **/

        @Inject(at = @At("HEAD"), method = "isZombified", cancellable = true)
        private static void chrysalis$piglinAvoidedTag(EntityType<?> entityType, CallbackInfoReturnable<Boolean> cir) {
            if (entityType.is(ChrysalisTags.PIGLIN_AVOIDED)) cir.setReturnValue(true);
        }
    }
}