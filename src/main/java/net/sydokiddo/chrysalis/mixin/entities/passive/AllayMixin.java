package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisCriteriaTriggers;
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
    private void chrysalis$allayDuplicationItemsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(itemStack.is(ChrysalisTags.DUPLICATES_ALLAYS));
    }

    /**
     * Allays being able to pick up items is now determined by the passiveGriefing gamerule rather than the mobGriefing gamerule.
     **/

    @ModifyArg(method = "wantsToPickUp", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private GameRules.Key<GameRules.BooleanValue> chrysalis$allayPassiveGriefingGamerule(GameRules.Key<GameRules.BooleanValue> oldValue) {
        return ChrysalisRegistry.RULE_PASSIVE_GRIEFING;
    }

    /**
     * Triggers the 'duplicate_allay' advancement criteria when a player duplicates an Allay
     **/

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/allay/Allay;duplicateAllay()V"))
    private void chrysalis$allayDuplicationCriteriaTrigger(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (player instanceof ServerPlayer serverPlayer) ChrysalisCriteriaTriggers.DUPLICATE_ALLAY.trigger(serverPlayer);
    }
}