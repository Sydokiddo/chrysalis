package net.junebug.chrysalis.mixin.misc;

import net.minecraft.client.main.Main;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.junebug.chrysalis.Chrysalis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(Main.class)
public class AWTHackMixin {

    /**
     * Sets the system's headless property to false so that images can be pasted to the clipboard.
     **/

    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void chrysalis$setAWTProperty(CallbackInfo info) {
        Chrysalis.LOGGER.info("Setting java.awt.headless property to false");
        System.setProperty("java.awt.headless", "false");
    }
}