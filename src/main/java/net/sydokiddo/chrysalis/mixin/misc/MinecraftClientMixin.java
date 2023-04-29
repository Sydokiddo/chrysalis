package net.sydokiddo.chrysalis.mixin.misc;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    // The menu framerate limit is no longer locked to 60 and now tied to the player's selected framerate limit

    @Inject(at=@At("HEAD"), method="getFramerateLimit()I", cancellable=true)
    private void chrysalis_uncap60FPSMenuLimit(CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(((Minecraft)(Object)this).getWindow().getFramerateLimit());
    }
}