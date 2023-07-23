package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    /**
     * The main menu's framerate is no longer hardcoded to 60 and now tied to the user's selected framerate limit in the options.
     **/

    @Inject(at=@At("HEAD"), method="getFramerateLimit()I", cancellable=true)
    private void chrysalis_uncap60FPSMenuLimit(CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(((Minecraft)(Object)this).getWindow().getFramerateLimit());
    }
}