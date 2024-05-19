package net.sydokiddo.chrysalis.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.sydokiddo.chrysalis.Chrysalis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(WorldOpenFlows.class)
public class WorldOpenFlowsMixin {

    @ModifyVariable(method = "confirmWorldCreation", at = @At("HEAD"), argsOnly = true, index = 4)
    private static boolean chrysalis$removeExperimentalAdviceOnCreation(boolean original) {
        if (Chrysalis.IS_DEBUG) return true;
        return original;
    }

    @ModifyVariable(method = "loadLevel", at = @At("HEAD"), argsOnly = true, index = 4)
    private boolean chrysalis$removeExperimentalAdviceOnLoad(boolean original) {
        if (Chrysalis.IS_DEBUG) return false;
        return original;
    }
}