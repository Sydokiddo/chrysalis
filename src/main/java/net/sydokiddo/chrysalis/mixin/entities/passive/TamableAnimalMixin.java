package net.sydokiddo.chrysalis.mixin.entities.passive;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.items.custom_items.debug_items.TameMobItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Wolf.class, Cat.class, Parrot.class})
public abstract class TamableAnimalMixin extends Animal {

    private TamableAnimalMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Prevents the interaction result from not properly working when taming tamable mobs.
     **/

    @Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
    private void chrysalis$allowPetInteractionWithTameMobItem(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (this.level().isClientSide() && player.getItemInHand(interactionHand).getItem() instanceof TameMobItem) {
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}