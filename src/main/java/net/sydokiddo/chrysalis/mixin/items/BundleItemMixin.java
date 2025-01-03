package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BundleItem.class)
public class BundleItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void chrysalis$preventBundleRightClickWhileEmpty(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        BundleContents bundleContents = player.getItemInHand(interactionHand).get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents != null && bundleContents.isEmpty()) cir.setReturnValue(InteractionResult.PASS);
    }
}