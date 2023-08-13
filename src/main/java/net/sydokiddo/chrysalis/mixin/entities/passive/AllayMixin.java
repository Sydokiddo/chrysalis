package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ModRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Allay.class)
public class AllayMixin {

    /**
     * Items that can duplicate Allays is now driven by the duplicates_allays tag.
     **/

    @Inject(at = @At("HEAD"), method = "isDuplicationItem", cancellable = true)
    private void chrysalis_allayDuplicationItemsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(itemStack.is(ChrysalisTags.DUPLICATES_ALLAYS));
    }

    /**
     * Allays being able to pick up items is now determined by the passiveGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "wantsToPickUp", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis_allayPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ModRegistry.RULE_PASSIVE_GRIEFING;
    }
}