package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.TameMobItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Horse.class)
public abstract class HorseMixin extends AbstractHorse {

    /**
     * Automatically tames Horses with the Tame Mob debug item.
     **/

    private HorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/Horse;makeMad()V"), cancellable = true)
    private void chrysalis$automaticallyTameHorse(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level().isClientSide() && player.getItemInHand(interactionHand).getItem() instanceof TameMobItem && !this.isTamed()) {
            this.tameWithName(player);
            TameMobItem.playTameEvents(player, this);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}