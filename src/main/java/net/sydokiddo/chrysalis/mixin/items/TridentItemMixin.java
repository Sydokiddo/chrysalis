package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.common.misc.CCriteriaTriggers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
public class TridentItemMixin {

    /**
     * Triggers the use_riptide_trident criteria trigger when the player uses a riptide trident.
     **/

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;startAutoSpinAttack(IFLnet/minecraft/world/item/ItemStack;)V"))
    private void chrysalis$useRiptideTridentCriteriaTrigger(ItemStack itemStack, Level level, LivingEntity livingEntity, int usedTicks, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof ServerPlayer serverPlayer) CCriteriaTriggers.USE_RIPTIDE_TRIDENT.get().trigger(serverPlayer);
    }
}